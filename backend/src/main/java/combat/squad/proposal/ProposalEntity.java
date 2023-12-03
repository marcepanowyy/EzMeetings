package combat.squad.proposal;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import combat.squad.event.EventEntity;
import combat.squad.user.UserEntity;
import combat.squad.vote.VoteEntity;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="proposals")
public class ProposalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date startDate;
    private Date endDate;

    @ManyToOne
    @JsonBackReference
    private EventEntity event;

    @CreatedDate
    private Date created;

    @OneToMany(mappedBy = "proposal")
    @JsonManagedReference
    private List<VoteEntity> votes;

    public ProposalEntity(EventEntity event, Date startDate, Date endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.event = event;
        this.votes = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public ProposalEntity() {
    }

}
