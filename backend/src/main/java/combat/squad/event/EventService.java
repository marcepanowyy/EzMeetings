package combat.squad.event;

import combat.squad.proposal.ProposalDto;
import combat.squad.proposal.ProposalEntity;
import combat.squad.proposal.ProposalRo;
import combat.squad.proposal.ProposalService;
import combat.squad.auth.UserEntity;
import combat.squad.auth.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
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
    public EventRo getEventDetails(String userEmail, UUID eventId) {

        UserEntity user = getUserByEmail(userEmail);
        EventEntity event = getEventById(eventId);

        if (!event.getParticipants().contains(user)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "User is not participating in this event");
        }
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

        EventEntity event = new EventEntity(
                eventDto.name(),
                eventDto.description(),
                eventDto.location(),
                user,
                new ArrayList<>()
        );

        List<ProposalDto> proposalDtos = eventDto.eventProposals();

        if (proposalDtos.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Event must have at least one proposal"
            );
        }

        event = this.eventRepository.save(event);

        List<ProposalEntity> proposalEntities = new ArrayList<>();

        for (ProposalDto proposalDTO : proposalDtos) {
            ProposalEntity proposalEntity = this.proposalService.createProposal(proposalDTO, event.getId());
            proposalEntities.add(proposalEntity);
        }

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

        event.getParticipants().add(user);
        this.eventRepository.save(event);

        user.getEvents().add(event);
        this.userRepository.save(user);

        return this.toEventRo(event, true, true, false, false);
    }

    // for now only the creator can update the event

    @Transactional
    public EventRo updateEvent(String userEmail, UUID eventId, EventDto eventDto) {

        UserEntity user = getUserByEmail(userEmail);
        EventEntity event = getEventById(eventId);

        if (!event.getCreator().getId().equals(user.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "User is not the creator of this event");
        }

        List<ProposalDto> proposalDtos = eventDto.eventProposals();

        if (proposalDtos.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Event must have at least one proposal"
            );
        }

        List<ProposalEntity> existingProposals = event.getEventProposals();
        List<ProposalEntity> proposalsToDelete = new ArrayList<>();

        // iterate over dtos, if an event has the same startDate as in dto - keep it, otherwise create a proposal.
        // remove the remaining (those that are not in dto and have no votes).

        for (ProposalDto proposalDto : proposalDtos) {

            // Existing Proposal: 2023-12-18 03:30:00.0
            // Proposal Dto: Mon Dec 18 03:30:00 CET 2023

            Optional<ProposalEntity> existingProposal = existingProposals.stream()
                    .filter(proposal -> proposal
                            .getStartDate()
                            .toInstant()
                            .equals(proposalDto
                                    .startDate()
                                    .toInstant()))
                    .findFirst();

            if (existingProposal.isEmpty()) {

                ProposalEntity proposalEntity = this.proposalService.createProposal(proposalDto, event.getId());
                existingProposals.add(proposalEntity);

            }
        }

        // throw error if the proposal has votes

        for (ProposalEntity proposal : existingProposals) {

            if (proposalDtos.stream()
                    .noneMatch(proposalDto -> proposalDto.startDate().equals(proposal.getStartDate()))) {

                if (!proposal.getVotes().isEmpty()) {

                    throw new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "Proposal has votes"
                    );

                } else {
                    proposalsToDelete.add(proposal);
                }
            }
        }

        existingProposals.removeAll(proposalsToDelete);
        proposalsToDelete.forEach(proposal -> this.proposalService.deleteProposal(proposal.getId()));

        event.setEventProposals(existingProposals);

        if (eventDto.name() != null) {
            event.setName(eventDto.name());
        }

        if (eventDto.description() != null) {
            event.setDescription(eventDto.description());
        }

        if (eventDto.location() != null) {
            event.setLocation(eventDto.location());
        }

        this.eventRepository.save(event);

        return this.toEventRo(event, true, false, false, false);

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
                ? Optional.ofNullable(event.getFinalProposalId())
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
