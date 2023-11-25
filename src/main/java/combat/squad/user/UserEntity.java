package combat.squad.user;

import combat.squad.event.EventEntity;
import combat.squad.proposal.ProposalEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nickname;

    @OneToMany(mappedBy = "creator")
    private List<EventEntity> events;

    @ManyToMany
    private List<ProposalEntity> selectedProposals;

    public UserEntity(String nickname){
        this.nickname = nickname;
        this.events = new ArrayList<>();
        this.selectedProposals = new ArrayList<>();
    }

    public UserEntity(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
