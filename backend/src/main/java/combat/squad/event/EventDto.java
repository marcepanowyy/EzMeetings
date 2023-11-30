package combat.squad.event;

import combat.squad.proposal.ProposalDto;

import java.util.Date;
import java.util.List;

public record EventDto(String name, String description, Date finalDate, String location, Long creatorId, List<ProposalDto> eventProposals) {

    @Override
    public String name() {
        return name;
    }

    @Override
    public String description() {
        return description;
    }

    @Override
    public Date finalDate() {
        return finalDate;
    }

    @Override
    public String location() {
        return location;
    }

    @Override
    public Long creatorId() {
        return creatorId;
    }

    @Override
    public List<ProposalDto> eventProposals() {
        return eventProposals;
    }
}
