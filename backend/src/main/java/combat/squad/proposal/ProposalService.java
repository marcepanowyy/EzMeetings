package combat.squad.proposal;

import combat.squad.event.EventEntity;
import combat.squad.event.EventRepository;
import combat.squad.event.EventService;
import combat.squad.vote.VoteService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProposalService {

    private final ProposalRepository proposalRepository;
    private final EventRepository eventRepository;
    private final VoteService voteService;

    public ProposalService(ProposalRepository proposalRepository, EventRepository eventRepository, VoteService voteService) {
        this.proposalRepository = proposalRepository;
        this.eventRepository = eventRepository;
        this.voteService = voteService;
    }

    public List<ProposalRo> getProposals() {
        return this.proposalRepository.findAll().stream().map(this::toProposalRo).toList();
    }

    public ProposalRo getProposalById(UUID id) {

        ProposalEntity proposal = this.proposalRepository.findById(id).orElseThrow();
        return this.toProposalRo(proposal);

    }

    public ProposalEntity createProposal(ProposalDto proposalDTO, UUID eventId){

        EventEntity event = this.eventRepository.findById(eventId).orElseThrow();

        ProposalEntity proposalEntity = new ProposalEntity(
                event,
                proposalDTO.startDate()
        );

        return this.proposalRepository.save(proposalEntity);
    }

    public ProposalRo toProposalRo(ProposalEntity proposal) {

        return new ProposalRo(
                proposal.getId(),
                proposal.getStartDate(),
                proposal.getVotes().stream().map(voteService::toVoteRo).toList()
        );

    }

}

