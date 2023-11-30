package combat.squad.vote;

import combat.squad.proposal.ProposalEntity;
import combat.squad.user.UserEntity;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;

@Entity
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

    public VoteEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserEntity getVoter() {
        return voter;
    }

    public void setVoter(UserEntity voter) {
        this.voter = voter;
    }

    public ProposalEntity getProposal() {
        return proposal;
    }

    public void setProposal(ProposalEntity proposal) {
        this.proposal = proposal;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
