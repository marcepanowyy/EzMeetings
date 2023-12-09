package combat.squad.event;

import combat.squad.proposal.ProposalDto;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.*;
public record EventDto(
        @NotBlank(message = "Name cannot be blank")
        String name,

        @NotBlank(message = "Description cannot be blank")
        String description,

        @NotBlank(message = "Location cannot be blank")
        String location,

        @NotEmpty(message = "Event proposals cannot be empty")
        List<@Valid ProposalDto> eventProposals
) {

}
