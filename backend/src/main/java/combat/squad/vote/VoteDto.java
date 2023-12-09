package combat.squad.vote;

import java.util.UUID;

public record VoteDto(
        UUID proposalId,
        State state) {

}
