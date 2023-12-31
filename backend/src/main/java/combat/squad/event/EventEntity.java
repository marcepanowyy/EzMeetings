package combat.squad.event;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import combat.squad.proposal.ProposalEntity;
import combat.squad.auth.UserEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "events")
public class EventEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    private String name;

    private String description;

    private String location;

    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "event")
    private List<ProposalEntity> eventProposals;

    @ManyToOne
    @JsonBackReference
    private UserEntity creator;

    @ManyToMany
    @JoinTable(
            name = "user_events",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<UserEntity> participants;

    @CreatedDate
    private Date created;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "final_proposal_id")
    private ProposalEntity finalProposal;

    public EventEntity(String name, String description, String location, UserEntity creator, List<ProposalEntity> eventProposals) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.creator = creator;
        this.eventProposals = eventProposals;
        this.participants = new ArrayList<>(List.of(creator));
        this.finalProposal = null;
    }
}
