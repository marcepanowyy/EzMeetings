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

    @OneToOne
    private ProposalEntity finalProposal;

    private String location;

    @OneToMany(mappedBy = "event")
    private List<ProposalEntity> eventProposals;

    @ManyToOne
    private UserEntity creator;

    @CreatedDate
    private Date created;

    public EventEntity(String name, String description, ProposalEntity finalProposal,String location, UserEntity creator, List<ProposalEntity> eventProposals) {
        this.name = name;
        this.description = description;
        this.finalProposal = finalProposal;
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

    public ProposalEntity getFinalDate() {
        return finalProposal;
    }

    public void setFinalProposal(ProposalEntity finalProposal) {
        this.finalProposal = finalProposal;
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

    public List<ProposalEntity> getProposals() {
        return eventProposals;
    }

    public void setProposals(List<ProposalEntity> proposals) {
        this.eventProposals = proposals;
    }
    public void addProposal(ProposalEntity proposal) {
        this.eventProposals.add(proposal);
    }
}
