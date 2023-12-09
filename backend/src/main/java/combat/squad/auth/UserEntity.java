package combat.squad.auth;

import combat.squad.auth.security.JwtTokenProvider;
import combat.squad.event.EventEntity;
import combat.squad.vote.VoteEntity;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

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

    @CreatedDate
    private Date created;

    @Enumerated(EnumType.STRING)
    private Role role;

    public UserEntity(String email, String password) {
        this.email = email;
        setPassword(password);
        this.createdEvents = new ArrayList<>();
        this.votes = new ArrayList<>();
        this.role = Role.USER;
    }

    public void setPassword(String password) {
        this.password = passwordEncoder.encode(password);
    }

    boolean comparePassword(String attempt){
        return passwordEncoder.matches(attempt, this.password);
    }

    public UserRo toUserRo(boolean showToken) {
        Optional<String> token = showToken ? Optional.ofNullable(jwtTokenProvider.generateToken(this)) : Optional.empty();
        return new UserRo(this.id, this.email, this.createdEvents, token, this.created);
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
