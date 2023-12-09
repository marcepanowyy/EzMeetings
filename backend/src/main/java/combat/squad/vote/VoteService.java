package combat.squad.vote;

import combat.squad.auth.UserRepository;
import combat.squad.event.EventEntity;
import combat.squad.event.EventRepository;
import combat.squad.proposal.ProposalEntity;
import combat.squad.proposal.ProposalRepository;
import combat.squad.proposal.ProposalService;
import combat.squad.auth.UserEntity;
import combat.squad.auth.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class VoteService {

    private final VoteRepository voteRepository;

    private final UserRepository userRepository;

    private final ProposalRepository proposalRepository;

    public VoteService(VoteRepository voteRepository, UserRepository userRepository, ProposalRepository proposalRepository) {
        this.voteRepository = voteRepository;
        this.userRepository = userRepository;
        this.proposalRepository = proposalRepository;
    }

    public List<VoteRo> getVotes() {
        return this.voteRepository.findAll().stream().map(this::toVoteRo).toList();
    }

    public List<VoteRo> vote(String userEmail, List<VoteDto> voteDtos){

        UserEntity user = this.userRepository.findByEmail(userEmail);

        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        return voteDtos.stream()
                .map(voteDto -> this.createVote(voteDto, user))
                .collect(Collectors.toList());
    }

    public VoteRo createVote(VoteDto voteDto, UserEntity voter) {

        UUID proposalId = voteDto.proposalId();

        ProposalEntity proposal = this.proposalRepository.findById(proposalId).orElseThrow();

        VoteEntity vote = new VoteEntity(
                voter,
                proposal,
                voteDto.state()
        );

        vote = this.voteRepository.save(vote);
        return this.toVoteRo(vote);
    }

    public VoteRo toVoteRo(VoteEntity voteEntity) {

        return new VoteRo(
                voteEntity.getId(),
                voteEntity.getVoter().getId(),
                voteEntity.getVoter().getEmail(),
                voteEntity.getState(),
                voteEntity.getCreated()
        );
    }

}
