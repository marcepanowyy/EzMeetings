package combat.squad.proposal;

import javax.validation.constraints.*;
import java.util.Date;

public record ProposalDto(

        @NotNull(message = "Start date cannot be null")
        @FutureOrPresent(message = "Start date must be present or future date")
        Date startDate

) {

}
