package combat.squad.vote;

import combat.squad.auth.UserRepository;
import combat.squad.proposal.ProposalEntity;
import combat.squad.proposal.ProposalRepository;
import combat.squad.auth.UserEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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

    @Transactional
    public List<VoteRo> vote(String userEmail, List<VoteDto> voteDtos){

        Optional<UserEntity> user = this.userRepository.findByEmail(userEmail);

        if (user.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "User not found");
        }

        return voteDtos.stream()
                .map(voteDto -> this.createVote(voteDto, user.get()))
                .collect(Collectors.toList());
    }

    public VoteRo createVote(VoteDto voteDto, UserEntity voter) {

        ProposalEntity proposal = this.getProposalById(voteDto.proposalId());
        Optional<VoteEntity> vote = this.voteRepository.findByVoterAndProposal(voter, proposal);

        // user has already voted on this proposal

        if (vote.isPresent()) {

            vote.get().setState(voteDto.state());
            vote = Optional.of(this.voteRepository.save(vote.get()));
            return this.toVoteRo(vote.get(), false);

        }

        // user has not voted on this proposal yet

        vote = Optional.of(new VoteEntity(
                voter,
                proposal,
                voteDto.state()
        ));

        vote = Optional.of(this.voteRepository.save(vote.get()));
        return this.toVoteRo(vote.get(), false);

    }

    public ProposalEntity getProposalById(UUID proposalId) {
        return this.proposalRepository.findById(proposalId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Proposal not found"));
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
