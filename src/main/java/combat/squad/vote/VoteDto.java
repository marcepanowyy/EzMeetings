package combat.squad.vote;

public record VoteDto(Long voterId, Long proposalId, String state) {
    @Override
    public Long voterId() {
        return voterId;
    }

    @Override
    public Long proposalId() {
        return proposalId;
    }
    @Override
    public String state() {
        return state;
    }
}
