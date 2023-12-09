package combat.squad.proposal;

import com.fasterxml.jackson.annotation.JsonBackReference;
import combat.squad.event.EventEntity;
import combat.squad.vote.VoteEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name="proposals")
public class ProposalEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    @FutureOrPresent(message = "Start date must be present or future date")
    private Date startDate;

    @JsonBackReference
    @ManyToOne
    private EventEntity event;

    @CreatedDate
    private Date created;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "proposal")
    private List<VoteEntity> votes;

    public ProposalEntity(EventEntity event, Date startDate) {
        this.startDate = startDate;
        this.event = event;
        this.votes = new ArrayList<>();
    }

}
