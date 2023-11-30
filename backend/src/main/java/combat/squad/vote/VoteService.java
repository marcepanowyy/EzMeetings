package combat.squad.vote;

import combat.squad.proposal.ProposalEntity;
import combat.squad.proposal.ProposalService;
import combat.squad.user.UserEntity;
import combat.squad.user.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public VoteEntity createVote(VoteDto voteDTO) {

        // check if user was invited to event

        // + check state

        // + check if user already voted

        Long voterId = voteDTO.voterId();
        UserEntity voter = this.userService.getUserById(voterId);

        Long proposalId = voteDTO.proposalId();
        ProposalEntity proposal = this.proposalService.getProposalById(proposalId);

        VoteEntity voteEntity = new VoteEntity(
                voter,
                proposal,
                voteDTO.state()
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
