package combat.squad.proposal;

import org.springframework.format.annotation.DateTimeFormat;
import static combat.squad.proposal.DateValidator.DATE_FORMAT;
import javax.validation.constraints.*;
import java.util.Date;

public record ProposalDto(

        @NotNull(message = "Start date cannot be null")
        @FutureOrPresent(message = "Start date must be present or future date")
//        @DateTimeFormat(pattern = DATE_FORMAT)
        Date startDate

) {

}
