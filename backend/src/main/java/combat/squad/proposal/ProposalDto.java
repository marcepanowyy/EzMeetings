package combat.squad.proposal;

import org.springframework.format.annotation.DateTimeFormat;
import static combat.squad.proposal.DateValidator.DATE_FORMAT;
import javax.validation.constraints.*;
import java.time.ZoneId;
import java.util.Date;


public record ProposalDto(
        @NotNull(message = "Start date cannot be null")
        @FutureOrPresent(message = "Start date must be present or future date")
        @DateTimeFormat(pattern = DATE_FORMAT)
        Date startDate,
        @NotNull(message = "End date cannot be null")
        @Future(message = "End date must be in the future")
        @DateTimeFormat(pattern = DATE_FORMAT)
        Date endDate
) {
    public ProposalDto {
            DateValidator.validateDates(
                       startDate,
                        endDate
            );
    }

    @Override
    public Date startDate() {
        return startDate;
    }
    @Override
    public Date endDate() {
        return endDate;
    }
}
