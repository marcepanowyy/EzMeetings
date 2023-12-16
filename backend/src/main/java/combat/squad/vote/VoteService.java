package combat.squad.vote;

import combat.squad.auth.UserRepository;
import combat.squad.event.EventEntity;
import combat.squad.event.EventRepository;
import combat.squad.proposal.ProposalEntity;
import combat.squad.proposal.ProposalRepository;
import combat.squad.auth.UserEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class VoteService {

    private final VoteRepository voteRepository;
    private final UserRepository userRepository;
    private final ProposalRepository proposalRepository;
    private final EventRepository eventRepository;

    public VoteService(
            VoteRepository voteRepository,
            UserRepository userRepository,
            ProposalRepository proposalRepository,
            EventRepository eventRepository
    ) {
        this.voteRepository = voteRepository;
        this.userRepository = userRepository;
        this.proposalRepository = proposalRepository;
        this.eventRepository = eventRepository;

    }

    public List<VoteRo> getVotes() {
        return this.voteRepository.findAll().stream()
                .map(vote -> toVoteRo(vote, true))
                .collect(Collectors.toList());
    }

    @Transactional
    public List<VoteRo> vote(String userEmail, UUID eventId, List<VoteDto> voteDtos){

        if(voteDtos.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "No votes provided");
        }

        UserEntity user = this.getUserByEmail(userEmail);
        EventEntity event = this.getEventById(eventId);

        // findVoteByUserAndProposal might return null if so, do not add to existing votes

        List<VoteEntity> existingVotes = event.getEventProposals().stream()
                .map(proposal -> this.findVoteByUserAndProposal(user, proposal))
                .filter(Objects::nonNull)
                .toList();

        List<VoteEntity> votesToDelete = existingVotes
                .stream()
                .filter(vote -> voteDtos.stream()
                        .noneMatch(voteDto -> voteDto.proposalId().equals(vote.getProposal().getId())))
                .toList();

        System.out.println("existingVotes: " + existingVotes.size());
        System.out.println("votesToDelete: " + votesToDelete.size());

        // delete votes that are not in the list of votes to create (or update if they already exist)

        votesToDelete.forEach(vote -> this.deleteVote(vote.getProposal().getId(), vote.getId()));

        return voteDtos.stream()
                .map(voteDto -> this.createVote(voteDto, user))
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

    public VoteEntity findVoteByUserAndProposal(UserEntity user, ProposalEntity proposal) {

        // if vote not found that means user has not voted yet so return null

        return this.voteRepository.findByVoterAndProposal(user, proposal)
                .orElse(null);

    }

    public void deleteVote(UUID proposalId, UUID voteId) {

        ProposalEntity proposal = this.getProposalById(proposalId);
        proposal.getVotes().removeIf(vote -> vote.getId().equals(voteId));
        this.proposalRepository.save(proposal);

        VoteEntity vote = this.findVoteById(voteId);
        this.voteRepository.delete(vote);

    }

    public VoteEntity findVoteById(UUID voteId) {

        return this.voteRepository.findById(voteId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Vote not found"));

    }

    public ProposalEntity getProposalById(UUID proposalId) {

        return this.proposalRepository.findById(proposalId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Proposal not found"));

    }

    public UserEntity getUserByEmail(String email) {

        return this.userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found"));

    }

    public EventEntity getEventById(UUID eventId) {

        return this.eventRepository.findById(eventId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Event not found"));

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
