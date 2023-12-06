package combat.squad.user;

import combat.squad.event.EventEntity;
import combat.squad.vote.VoteEntity;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.*;


@Data
@NoArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity {

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    @Column(unique=true)
    private String email;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private String password;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "creator")
    private List<EventEntity> events;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "voter")
    private List<VoteEntity> votes;

    @CreatedDate
    private Date created;

    public UserEntity(String email, String password) {
        this.email = email;
        setPassword(password);
        this.events = new ArrayList<>();
        this.votes = new ArrayList<>();
    }

    public void setPassword(String password) {
        this.password = passwordEncoder.encode(password);
    }

    boolean comparePassword(String attempt){
        return passwordEncoder.matches(attempt, this.password);
    }

    private String generateToken() {

        Date expirationDate = new Date(System.currentTimeMillis() + (7 * 24 * 60 * 60 * 1000)); // 7 days

        return Jwts.builder()
                .setSubject(this.email)
                .claim("id", this.id)
                .claim("username", this.email)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, "rememberToAddThisToEnvFile")
                .compact();
    }

    public UserRo toUserRo(boolean showToken) {
        Optional<String> token = showToken ? Optional.ofNullable(generateToken()) : Optional.empty();
        return new UserRo(this.id, this.email, this.events, token, this.created);
    }


}
