package combat.squad.event;

import combat.squad.proposal.ProposalDto;
import combat.squad.proposal.ProposalEntity;
import combat.squad.proposal.ProposalRo;
import combat.squad.proposal.ProposalService;
import combat.squad.auth.UserEntity;
import combat.squad.auth.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final ProposalService proposalService;

    public EventService(EventRepository eventRepository, UserRepository userRepository, ProposalService proposalService) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.proposalService = proposalService;
    }

    public List<EventRo> getEvents() {
        return this.eventRepository.findAll().stream()
                .map(event -> toEventRo(event, false, true, false, false))
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteEvent(String username, UUID eventId) {

        UserEntity user = getUserByEmail(username);
        EventEntity event = getEventById(eventId);

        checkUserIsEventCreator(user, event);

        user.getCreatedEvents().remove(event);
        this.userRepository.save(user);
        this.eventRepository.delete(event);

    }

    @Transactional
    public EventRo getEventDetails(String userEmail, UUID eventId) {

        UserEntity user = getUserByEmail(userEmail);
        EventEntity event = getEventById(eventId);

        checkUserParticipation(user, event);

        return toEventRo(event, true, true, true, true);

    }

    @Transactional
    public List<EventRo> getAllUserEvents(String userEmail) {

        UserEntity user = getUserByEmail(userEmail);

        return user.getEvents().stream()
                .map(event -> toEventRo(event, false, true, false, false))
                .collect(Collectors.toList());

    }

    public List<EventRo> getCreatedUserEvents(String userEmail) {

        UserEntity user = getUserByEmail(userEmail);

        return user.getCreatedEvents().stream()
                .map(event -> toEventRo(event, false, false, false, false))
                .collect(Collectors.toList());

    }

    public EventRo createEvent(String userEmail, EventDto eventDto) {

        UserEntity user = getUserByEmail(userEmail);

        List<ProposalDto> proposalDtos = eventDto.eventProposals();
        checkAtLeastOneProposal(proposalDtos);

        EventEntity event = createEventEntity(user, eventDto);
        event = this.eventRepository.save(event);

        List<ProposalEntity> proposalEntities = createAndSaveProposals(proposalDtos, event.getId());

        event.setEventProposals(proposalEntities);
        this.eventRepository.save(event);

        return this.toEventRo(event, true, false, false, false);

    }

    @Transactional
    public EventRo participateInEvent(String userEmail, UUID eventId) {

        UserEntity user = getUserByEmail(userEmail);
        EventEntity event = getEventById(eventId);

        if (event.getParticipants().contains(user)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "User is already participating in this event");
        }

        addParticipantToEvent(user, event);

        return this.toEventRo(event, true, true, false, false);

    }

    // only the creator can update the event

    @Transactional
    public EventRo updateEvent(String userEmail, UUID eventId, EventDto eventDto) {

        UserEntity user = getUserByEmail(userEmail);
        EventEntity event = getEventById(eventId);

        checkUserIsEventCreator(user, event);

        List<ProposalDto> proposalDtos = eventDto.eventProposals();
        checkAtLeastOneProposal(proposalDtos);

        List<ProposalEntity> oldProposals = event.getEventProposals();
        checkEventProposalsForModifications(oldProposals, proposalDtos);

        List<ProposalEntity> newProposals = createNewProposals(proposalDtos, oldProposals, event.getId());
        removeUnusedProposals(oldProposals, newProposals);

        event = updateAndSaveEventEntity(event, newProposals, eventDto);
        return this.toEventRo(event, true, false, false, false);

    }

    @Transactional
    public EventRo finalizeEvent(String userEmail, UUID eventId, UUID proposalId) {

        UserEntity user = getUserByEmail(userEmail);
        EventEntity event = getEventById(eventId);
        ProposalEntity proposal = this.proposalService.getProposalById(proposalId);

        checkUserIsEventCreator(user, event);
        checkProposalBelongsToEvent(proposal, event);

        event.setFinalProposal(proposal);
        this.eventRepository.save(event);

        return this.toEventRo(event, true, true, true, true);

    }

    private EventEntity updateAndSaveEventEntity(EventEntity event, List<ProposalEntity> newProposals, EventDto eventDto) {

        event.setEventProposals(newProposals);
        event.setName(eventDto.name());
        event.setDescription(eventDto.description());
        event.setLocation(eventDto.location());
        return this.eventRepository.save(event);

    }

    private List<ProposalEntity> createNewProposals(List<ProposalDto> proposalDtos, List<ProposalEntity> oldProposals, UUID eventId) {

        List<ProposalEntity> newProposals = new ArrayList<>();

        for (ProposalDto proposalDto : proposalDtos) {

            Optional<ProposalEntity> existingProposal = findExistingProposal(proposalDto, oldProposals);

            if (existingProposal.isEmpty()) {

                ProposalEntity proposalEntity = this.proposalService.createProposal(proposalDto, eventId);
                newProposals.add(proposalEntity);

            } else {

                newProposals.add(existingProposal.get());

            }
        }

        return newProposals;

    }

    private Optional<ProposalEntity> findExistingProposal(ProposalDto proposalDto, List<ProposalEntity> oldProposals) {

        return oldProposals.stream()
                .filter(proposal -> proposal
                        .getStartDate()
                        .toInstant()
                        .equals(proposalDto
                                .startDate()
                                .toInstant()))
                .findFirst();

    }

    private void removeUnusedProposals(List<ProposalEntity> oldProposals, List<ProposalEntity> newProposals) {

        List<ProposalEntity> proposalsToRemove = oldProposals
                .stream()
                .filter(oldProposal -> newProposals
                        .stream()
                        .noneMatch(newProposal -> newProposal
                                .getId()
                                .equals(oldProposal
                                        .getId())))

                .toList();

        proposalsToRemove.forEach(proposal -> proposalService.deleteProposal(proposal.getId()));

    }

    private void addParticipantToEvent(UserEntity user, EventEntity event) {

        event.getParticipants().add(user);
        this.eventRepository.save(event);

        user.getEvents().add(event);
        this.userRepository.save(user);

    }

    private EventEntity createEventEntity(UserEntity user, EventDto eventDto) {

        return new EventEntity(
                eventDto.name(),
                eventDto.description(),
                eventDto.location(),
                user,
                new ArrayList<>()
        );
    }

    private List<ProposalEntity> createAndSaveProposals(List<ProposalDto> proposalDtos, UUID eventId) {

        List<ProposalEntity> proposalEntities = new ArrayList<>();

        for (ProposalDto proposalDTO : proposalDtos) {
            ProposalEntity proposalEntity = proposalService.createProposal(proposalDTO, eventId);
            proposalEntities.add(proposalEntity);
        }

        return proposalEntities;

    }

    private void checkUserParticipation(UserEntity user, EventEntity event) {
        if (!event.getParticipants().contains(user)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "User is not participating in this event");
        }
    }

    private void checkUserIsEventCreator(UserEntity user, EventEntity event) {
        if (!event.getCreator().getId().equals(user.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "User is not the creator of this event");
        }
    }

    private void checkAtLeastOneProposal(List<ProposalDto> proposalDtos) {
        if (proposalDtos.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Event must have at least one proposal"
            );
        }
    }

    public void checkEventProposalsForModifications(List<ProposalEntity> oldProposals, List<ProposalDto> proposalDtos) {

        for (ProposalEntity proposal : oldProposals) {
            processProposal(proposal, proposalDtos);
        }

    }

    private void processProposal(ProposalEntity proposal, List<ProposalDto> proposalDtos) {

        if (proposalDtos.stream()
                .noneMatch(
                        proposalDto -> proposalDto
                                .startDate()
                                .equals(proposal.getStartDate()))) {

            checkProposalHasNoVotes(proposal);

        }
    }

    private void checkProposalHasNoVotes(ProposalEntity proposal) {
        if (!proposal.getVotes().isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Proposal has votes, cannot be modified"
            );
        }
    }

    private void checkProposalBelongsToEvent(ProposalEntity proposal, EventEntity event) {

        if (!proposal.getEvent().getId().equals(event.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Proposal does not belong to this event"
            );
        }

    }

    public UserEntity getUserByEmail(String userEmail) {

        return this.userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found"));

    }

    private EventEntity getEventById(UUID eventId) {

        return this.eventRepository.findById(eventId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Event not found"));

    }

    public EventRo toEventRo(
            EventEntity event,
            Boolean showProposals,
            Boolean showCreator,
            Boolean showFinalProposalId,
            Boolean showVotes
    ) {

        List<ProposalEntity> proposals = event.getEventProposals();

        Optional<List<ProposalRo>> proposalRoList = showProposals
                ? Optional.of(proposals.stream()
                .map(proposal -> proposalService.toProposalRo(proposal, showVotes))
                .collect(Collectors.toList()))
                : Optional.empty();

        Optional<UUID> creatorId = showCreator
                ? Optional.of(event.getCreator().getId())
                : Optional.empty();

        Optional<String> creatorEmail = showCreator
                ? Optional.of(event.getCreator().getEmail())
                : Optional.empty();

        Optional<UUID> finalProposalId = showFinalProposalId
                ? Optional.ofNullable(event.getFinalProposal().getId())
                : Optional.empty();

        return new EventRo(
                event.getId(),
                event.getName(),
                event.getDescription(),
                event.getLocation(),
                proposalRoList,
                creatorId,
                creatorEmail,
                finalProposalId
        );
    }
}
