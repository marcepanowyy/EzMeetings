package combat.squad.vote;

import combat.squad.proposal.ProposalEntity;
import combat.squad.auth.UserEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "votes")
public class VoteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private UserEntity voter;

    @ManyToOne
    private ProposalEntity proposal;

    @CreatedDate
    private Date created;

    private String state;

    public VoteEntity(UserEntity voter, ProposalEntity proposal, String state) {
        this.voter = voter;
        this.proposal = proposal;
        this.state = state;
    }

}
