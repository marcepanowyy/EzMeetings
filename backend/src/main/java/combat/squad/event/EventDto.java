package combat.squad.event;

import combat.squad.proposal.ProposalDto;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public record EventDto(

        @NotBlank(message = "Name cannot be blank")
        @Size(min = 3, max = 15, message = "Name must be between 3 and 15 characters")
        String name,

        @NotBlank(message = "Description cannot be blank")
        @Size(min = 20, max = 200, message = "Description must be between 20 and 200 characters")
        String description,

        @NotBlank(message = "Location cannot be blank")
        @Size(min = 3, max = 20, message = "Location must be between 3 and 20 characters")
        String location,

        @NotEmpty(message = "Event proposals cannot be empty")
        List<ProposalDto> eventProposals
) {

}

