package combat.squad.vote;

import combat.squad.proposal.ProposalEntity;
import combat.squad.auth.UserEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "votes")
public class VoteEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    @ManyToOne
    private UserEntity voter;

    @ManyToOne
    private ProposalEntity proposal;

    @CreatedDate
    private Date created;

    @Enumerated(EnumType.STRING)
    private State state;

    public VoteEntity(UserEntity voter, ProposalEntity proposal, State state) {
        this.voter = voter;
        this.proposal = proposal;
        this.state = state;
    }

}
