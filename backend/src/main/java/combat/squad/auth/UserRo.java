package combat.squad.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import combat.squad.event.EventEntity;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import java.util.Optional;

public record UserRo(

        UUID id,
        String email,

        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        Optional<String> token,

        Date created

) {
}