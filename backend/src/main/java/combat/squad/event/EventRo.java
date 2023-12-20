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

        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        Optional<List<ProposalRo>> eventProposals,

        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        Optional<UUID> creatorId,

        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        Optional<String> creatorEmail,

        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        Optional<UUID> finalProposalId

) {

}