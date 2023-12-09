package combat.squad.vote;

import java.util.UUID;

public record VoteDto(UUID voterId, UUID proposalId, String state) {
//    @Override
//    public Long voterId() {
//        return voterId;
//    }
//    @Override
//    public Long proposalId() {
//        return proposalId;
//    }
//    @Override
//    public String state() {
//        return state;
//    }
}
