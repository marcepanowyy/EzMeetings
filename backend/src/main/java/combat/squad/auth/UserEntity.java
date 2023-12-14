package combat.squad.auth;

import combat.squad.auth.security.JwtTokenProvider;
import combat.squad.event.EventEntity;
import combat.squad.shared.role.Role;
import combat.squad.vote.VoteEntity;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.*;

@Entity
@Data
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "users")
public class UserEntity implements UserDetails {

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private static JwtTokenProvider jwtTokenProvider = new JwtTokenProvider();

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    @Column(unique=true)
    private String email;

    private String password;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "creator")
    private List<EventEntity> createdEvents;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "voter")
    private List<VoteEntity> votes;

    @ManyToMany(mappedBy = "participants")
    private List<EventEntity> events;

    @CreatedDate
    private Date created;

    @Enumerated(EnumType.STRING)
    private Role role;

    public UserEntity(String email, String password) {
        this.email = email;
        setPassword(password);
        this.votes = new ArrayList<>();
        this.role = Role.USER;
        this.createdEvents = new ArrayList<>();
        this.events = new ArrayList<>();

    }

    public void setPassword(String password) {
        this.password = passwordEncoder.encode(password);
    }

    boolean comparePassword(String attempt){
        return passwordEncoder.matches(attempt, this.password);
    }

    public UserRo toUserRo(Boolean showToken) {
        Optional<String> token = showToken ? Optional.of(jwtTokenProvider.generateToken(this)) : Optional.empty();
        return new UserRo(this.id, this.email, token, this.created);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {return List.of(new SimpleGrantedAuthority(role.name()));}

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
