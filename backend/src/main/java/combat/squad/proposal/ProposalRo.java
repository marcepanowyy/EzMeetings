package combat.squad.proposal;

import combat.squad.vote.VoteRo;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public record ProposalRo(

        UUID id,
        Date startDate,
        List<VoteRo> votes

) {
}
