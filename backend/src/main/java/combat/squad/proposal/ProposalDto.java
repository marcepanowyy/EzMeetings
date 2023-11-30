package combat.squad.proposal;

import java.util.Date;

public record ProposalDto(Date startDate, Date endDate) {

    @Override
    public Date startDate() {
        return startDate;
    }

    @Override
    public Date endDate() {
        return endDate;
    }
}
