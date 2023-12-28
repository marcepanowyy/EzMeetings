package combat.squad;

import combat.squad.auth.security.JwtTokenProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing // enables @CreatedDate and @LastModifiedDate
@SpringBootApplication
public class CombatSquadProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(CombatSquadProjectApplication.class, args);
	}

}
