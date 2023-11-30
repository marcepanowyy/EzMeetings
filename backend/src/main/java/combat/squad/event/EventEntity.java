package combat.squad.event;

import combat.squad.proposal.ProposalEntity;
import combat.squad.user.UserEntity;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "events")
public class EventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    public List<ProposalEntity> getEventProposals() {
        return eventProposals;
    }

    public UserEntity getCreator() {
        return creator;
    }

    public Date getCreated() {
        return created;
    }

    private Date finalDate;

    private String location;

    @OneToMany(mappedBy = "event")
    private List<ProposalEntity> eventProposals;

    @ManyToOne
    private UserEntity creator;

    @CreatedDate
    private Date created;

    public EventEntity(String name, String description, Date finalDate, String location, UserEntity creator, List<ProposalEntity> eventProposals) {
        this.name = name;
        this.description = description;
        this.finalDate = finalDate;
        this.location = location;
        this.creator = creator;
        this.eventProposals = eventProposals;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getFinalDate() {
        return finalDate;
    }

    public void setFinalDate(Date finalDate) {
        this.finalDate = finalDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setEventProposals(List<ProposalEntity> eventProposals) {
        this.eventProposals = eventProposals;
    }

    public EventEntity() {
    }

}
