package combat.squad.proposal;

import combat.squad.event.EventEntity;
import combat.squad.event.EventRepository;
import combat.squad.event.EventService;
import combat.squad.vote.VoteRo;
import combat.squad.vote.VoteService;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.swing.text.html.Option;
import javax.validation.Valid;
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

    public ProposalEntity createProposal(@Valid ProposalDto proposalDTO, UUID eventId){

        Optional<EventEntity> event = this.eventRepository.findById(eventId);

        if (event.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Event not found");
        }

        ProposalEntity proposal = new ProposalEntity(
                event.get(),
                proposalDTO.startDate()
        );

        return this.proposalRepository.save(proposal);
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

