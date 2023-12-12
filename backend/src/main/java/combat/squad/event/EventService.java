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

    @Autowired
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
            throw new IllegalArgumentException("User is not participating in this event");
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

        List<ProposalDto> proposals = eventDto.eventProposals();

        if (proposals.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Event must have at least one proposal"
            );
        }

        event = this.eventRepository.save(event);

        List<ProposalEntity> proposalEntities = new ArrayList<>();

        for (ProposalDto proposalDTO : proposals) {
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

    private UserEntity getUserByEmail(String userEmail) {
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
