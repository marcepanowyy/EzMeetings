package combat.squad.vote;

import combat.squad.auth.UserEntity;
import combat.squad.event.EventEntity;
import combat.squad.proposal.ProposalEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface VoteRepository extends JpaRepository<VoteEntity, UUID> {

    Optional<VoteEntity> findByVoterAndProposal(UserEntity voter, ProposalEntity proposal);

}
