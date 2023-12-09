package combat.squad.vote;

import combat.squad.proposal.ProposalEntity;
import combat.squad.proposal.ProposalService;
import combat.squad.auth.UserEntity;
import combat.squad.auth.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class VoteService {

    private final VoteRepository voteRepository;

    private final UserService userService;
    private final ProposalService proposalService;

    public VoteService(VoteRepository voteRepository, UserService userService, ProposalService proposalService) {
        this.voteRepository = voteRepository;
        this.userService = userService;
        this.proposalService = proposalService;
    }

    public List<VoteEntity> getVotes() {
        return this.voteRepository.findAll();
    }

    public VoteEntity getVoteById(Long id) {
        return this.voteRepository.findById(id).orElseThrow();
    }

    public VoteEntity createVote(VoteDto voteDto) {

        // check if user was invited to event

        // + check state

        // + check if user already voted

        UUID voterId = voteDto.voterId();
        UserEntity voter = this.userService.getUserById(voterId);

        UUID proposalId = voteDto.proposalId();
        ProposalEntity proposal = this.proposalService.getProposalById(proposalId);

        VoteEntity voteEntity = new VoteEntity(
                voter,
                proposal,
                voteDto.state()
        );
        return this.voteRepository.save(voteEntity);
    }

    public VoteEntity updateVote(VoteEntity voteEntity) {
        return this.voteRepository.save(voteEntity);
    }

    public void deleteVote(Long id) {
        this.voteRepository.deleteById(id);
    }


}
