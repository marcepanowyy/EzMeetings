package combat.squad.proposal;

import com.fasterxml.jackson.annotation.JsonInclude;
import combat.squad.vote.VoteRo;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public record ProposalRo(

        UUID id,
        Date startDate,

        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        Optional<List<VoteRo>> votes

) {
}
