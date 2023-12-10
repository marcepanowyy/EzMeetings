package combat.squad.proposal;

import combat.squad.event.EventEntity;
import combat.squad.event.EventRepository;
import combat.squad.event.EventService;
import combat.squad.vote.VoteRo;
import combat.squad.vote.VoteService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
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

        return this.proposalRepository.findAll().stream()
                .map(proposal -> toProposalRo(proposal, true))
                .collect(Collectors.toList());
    }

    public ProposalRo getProposalById(UUID id) {

        ProposalEntity proposal = this.proposalRepository.findById(id).orElseThrow();
        return this.toProposalRo(proposal, true);

    }

    public ProposalEntity createProposal(ProposalDto proposalDTO, UUID eventId){

        EventEntity event = this.eventRepository.findById(eventId).orElseThrow();

        ProposalEntity proposalEntity = new ProposalEntity(
                event,
                proposalDTO.startDate()
        );

        return this.proposalRepository.save(proposalEntity);
    }

    public ProposalRo toProposalRo(
            ProposalEntity proposal,
            Boolean showVotes

    ) {

        Optional<List<VoteRo>> votes = showVotes
                ? Optional.of(proposal.getVotes().stream().map(vote -> voteService.toVoteRo(vote, true)).toList())
                : Optional.empty();


        return new ProposalRo(
                proposal.getId(),
                proposal.getStartDate(),
                votes
        );

    }

}

