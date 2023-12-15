package combat.squad.vote;

import combat.squad.shared.state.State;
import combat.squad.shared.state.StateEnumValidation;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public record VoteDto(

        @NotNull(message = "Proposal id cannot be null")
        UUID proposalId,

        @StateEnumValidation(message = "State must be one of the following: YES, IF_NEED_BE, NO")
        State state

) {

}
