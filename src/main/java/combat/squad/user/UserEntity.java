package combat.squad.user;

import combat.squad.event.EventEntity;
import combat.squad.proposal.ProposalEntity;
import combat.squad.vote.VoteEntity;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true)
    private String nickname;

    @OneToMany(mappedBy = "creator")
    private List<EventEntity> events;

    @OneToMany(mappedBy = "voter")
    private List<VoteEntity> votes;

    @CreatedDate
    private Date created;

    public UserEntity(String nickname){
        this.nickname = nickname;
        this.events = new ArrayList<>();
        this.votes = new ArrayList<>();
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
