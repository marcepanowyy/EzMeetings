package combat.squad.vote;

import java.util.Date;

public record VoteRo(Long voteId, String voterNickname, Date startDate, Date endDate, String state) {
    @Override
    public Long voteId() {
        return voteId;
    }

    @Override
    public String voterNickname() {
        return voterNickname;
    }

    @Override
    public Date startDate() {
        return startDate;
    }

    @Override
    public Date endDate() {
        return endDate;
    }

    @Override
    public String state() {
        return state;
    }
}
