package combat.squad.event;

import combat.squad.proposal.ProposalDto;
import combat.squad.proposal.ProposalEntity;
import combat.squad.proposal.ProposalService;
import combat.squad.user.UserEntity;
import combat.squad.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;

    private final UserService userService;
    private final ProposalService proposalService;

    @Autowired
    public EventService(EventRepository eventRepository, UserService userService, ProposalService proposalService) {
        this.eventRepository = eventRepository;
        this.userService = userService;
        this.proposalService = proposalService;
    }

    public List<EventEntity> getEvents() {
        return this.eventRepository.findAll();
    }

    public EventEntity getEventById(Long id) {
        return this.eventRepository.findById(id).orElseThrow();
    }

    public EventEntity createEvent(EventDto eventDto) {

        Long userId = eventDto.creatorId();
        UserEntity user = this.userService.getUserById(userId);

        EventEntity eventEntity = new EventEntity(
                eventDto.name(),
                eventDto.description(),
                eventDto.finalDate(),
                eventDto.location(),
                user,
                new ArrayList<>()
        );

        eventEntity = this.eventRepository.save(eventEntity);

        List<ProposalDto> proposals = eventDto.eventProposals();
        List<ProposalEntity> proposalEntities = new ArrayList<>();

        for (ProposalDto proposalDTO : proposals) {
            ProposalEntity proposalEntity = this.proposalService.createProposal(proposalDTO, eventEntity.getId());
            proposalEntities.add(proposalEntity);
        }

        eventEntity.getEventProposals().addAll(proposalEntities);
        return this.eventRepository.save(eventEntity);
    }

    public EventEntity updateEvent(EventEntity eventEntity) {
        return this.eventRepository.save(eventEntity);
    }

    public void deleteEvent(Long id) {
        this.eventRepository.deleteById(id);
    }

}
