package combat.squad.proposal;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProposalRepository extends JpaRepository<ProposalEntity, UUID> {
}
