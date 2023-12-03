package combat.squad.proposal;

import combat.squad.event.EventEntity;
import combat.squad.event.EventRepository;
import combat.squad.event.EventService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProposalService {
    private final ProposalRepository proposalRepository;
    private final EventRepository eventRepository;

    public ProposalService(ProposalRepository proposalRepository, EventRepository eventRepository) {
        this.proposalRepository = proposalRepository;
        this.eventRepository = eventRepository;
    }

    public List<ProposalEntity> getProposals() {
        return this.proposalRepository.findAll();
    }

    public ProposalEntity getProposalById(Long id) {
        return this.proposalRepository.findById(id).orElseThrow();
    }

    public ProposalEntity createProposal(ProposalDto proposalDTO, Long eventId){
        EventEntity event = this.eventRepository.findById(eventId).orElseThrow();

        ProposalEntity proposalEntity = new ProposalEntity(
                event,
                proposalDTO.startDate(),
                proposalDTO.endDate()
        );

        return this.proposalRepository.save(proposalEntity);
    }

    public void deleteProposal(Long id) {
        this.proposalRepository.deleteById(id);
    }

}
