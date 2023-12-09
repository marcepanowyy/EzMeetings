package combat.squad.event;

import com.fasterxml.jackson.annotation.JsonInclude;
import combat.squad.proposal.ProposalRo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public record EventRo(

    UUID id,
    String name,
    String description,
    String location,
//    ProposalRo finalProposal,
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Optional<List<ProposalRo>> eventProposals

) {

}
