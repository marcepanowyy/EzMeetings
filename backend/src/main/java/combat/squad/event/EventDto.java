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

        ProposalDto finalProposal,

        @NotBlank(message = "Location cannot be blank")
        String location,

        @NotNull(message = "Creator ID cannot be null")
        UUID creatorId,

        @NotEmpty(message = "Event proposals cannot be empty")
        List<@Valid ProposalDto> eventProposals
) {


//    @Override
//    public String name() {
//        return name;
//    }
//
//    @Override
//    public String description() {
//        return description;
//    }
//
//    @Override
//    public ProposalDto finalProposal() {
//        return finalProposal;
//    }
//
//    @Override
//    public String location() {
//        return location;
//    }
//
//    @Override
//    public UUID creatorId() {
//        return creatorId;
//    }
//
//    @Override
//    public List<ProposalDto> eventProposals() {
//        return eventProposals;
//    }

}
