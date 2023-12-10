package combat.squad.event;

import combat.squad.proposal.ProposalDto;
import combat.squad.proposal.ProposalEntity;
import combat.squad.proposal.ProposalRo;
import combat.squad.proposal.ProposalService;
import combat.squad.auth.UserEntity;
import combat.squad.auth.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public EventEntity getEventById(UUID id) {
        return this.eventRepository.findById(id).orElseThrow();
    }

    public EventRo getEventDetails(String userEmail, UUID eventId) {

//         TODO check if user is a creator of this event or a participant

//        UserEntity user = this.userRepository.findByEmail(userEmail);
//
//        if (user == null) {
//            throw new IllegalArgumentException("User not found");
//        }

        EventEntity event = this.eventRepository.findById(eventId).orElseThrow();

        return this.toEventRo(event, true, true, true, true);
    }


    public List<EventRo> getEventsByUser(String userEmail) {

        UserEntity user = this.userRepository.findByEmail(userEmail);

        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        return user.getCreatedEvents().stream()
                .map(event -> toEventRo(event, false, false, false, false))
                .collect(Collectors.toList());
    }

    public EventRo createEvent(String userEmail, EventDto eventDto){

        UserEntity user = this.userRepository.findByEmail(userEmail);

        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        EventEntity event = new EventEntity(
                eventDto.name(),
                eventDto.description(),
                eventDto.location(),
                user,
                new ArrayList<>()
        );

        List<ProposalDto> proposals = eventDto.eventProposals();

        if (proposals.isEmpty()) {
            throw new IllegalArgumentException("Event must have at least one proposal");
        }

        event = this.eventRepository.save(event);

        List<ProposalEntity> proposalEntities = new ArrayList<>();

        for (ProposalDto proposalDTO : proposals) {
            ProposalEntity proposalEntity = this.proposalService.createProposal(proposalDTO, event.getId());
            proposalEntities.add(proposalEntity);
        }

        event.getEventProposals().addAll(proposalEntities);
        this.eventRepository.save(event);

        return this.toEventRo(event, true, false, false, false);
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
