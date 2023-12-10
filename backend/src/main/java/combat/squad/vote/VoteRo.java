package combat.squad.vote;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

public record VoteRo(

        UUID voteId,

        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        Optional<UUID> voterId,

        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        Optional<String> voterEmail,

        State state,
        Date created

) {

}
