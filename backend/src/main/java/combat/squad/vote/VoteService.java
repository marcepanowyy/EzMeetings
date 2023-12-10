package combat.squad.vote;

import combat.squad.auth.UserRepository;
import combat.squad.proposal.ProposalEntity;
import combat.squad.proposal.ProposalRepository;
import combat.squad.auth.UserEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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
        return this.voteRepository.findAll().stream()
                .map(vote -> toVoteRo(vote, true))
                .collect(Collectors.toList());
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

        Optional<ProposalEntity> proposal = this.proposalRepository.findById(voteDto.proposalId());

        if(proposal.isEmpty()){
            throw new IllegalArgumentException("Proposal not found");
        }

        VoteEntity vote = this.voteRepository.findByVoterAndProposal(voter, proposal.get());

        if (vote != null) {

            // user has already voted on this proposal

            vote.setState(voteDto.state());
            vote = this.voteRepository.save(vote);
            return this.toVoteRo(vote, false);

        }

        // user has not voted on this proposal yet

        vote = new VoteEntity(
                voter,
                proposal.get(),
                voteDto.state()
        );

        vote = this.voteRepository.save(vote);
        return this.toVoteRo(vote, false);
    }

    public VoteRo toVoteRo(VoteEntity voteEntity, Boolean showVoter) {

        Optional<String> voterEmail = showVoter
                ? Optional.of(voteEntity.getVoter().getEmail())
                : Optional.empty();

        Optional<UUID> voterId = showVoter
                ? Optional.of(voteEntity.getVoter().getId())
                : Optional.empty();

        return new VoteRo(
                voteEntity.getId(),
                voterId,
                voterEmail,
                voteEntity.getState(),
                voteEntity.getCreated()

        );
    }

}
