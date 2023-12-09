package combat.squad.vote;

import java.util.Date;
import java.util.UUID;

public record VoteRo(

        UUID voteId,
        UUID voterId,
        String voterEmail,
        State state,
        Date created

) {

}
