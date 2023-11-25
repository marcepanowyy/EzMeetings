package combat.squad.proposal;

import combat.squad.event.EventEntity;
import combat.squad.user.UserEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="proposals")
public class ProposalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private EventEntity event;

    @ManyToMany(mappedBy = "selectedProposals")
    private List<UserEntity> participants;

    private Date startDate;
    private Date endDate;

    public ProposalEntity(Date startDate, Date endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
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
