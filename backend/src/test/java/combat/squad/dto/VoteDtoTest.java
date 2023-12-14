package combat.squad.dto;

import combat.squad.shared.state.State;
import combat.squad.vote.VoteDto;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VoteDtoTest {

    @Test
    void testValidVoteDto() {
        VoteDto voteDto = new VoteDto(UUID.randomUUID(), State.YES);

        Set<ConstraintViolation<VoteDto>> violations = validate(voteDto);
        assertEquals(0, violations.size());
    }

    @Test
    void testInvalidVoteDtoNullProposalId() {
        VoteDto voteDto = new VoteDto(null, State.YES);

        Set<ConstraintViolation<VoteDto>> violations = validate(voteDto);
        assertEquals(1, violations.size());
    }

    @Test
    void testInvalidVoteDtoNullState() {
        VoteDto voteDto = new VoteDto(UUID.randomUUID(), null);

        Set<ConstraintViolation<VoteDto>> violations = validate(voteDto);
        assertEquals(1, violations.size());
    }

    private Set<ConstraintViolation<VoteDto>> validate(VoteDto voteDto) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        return validator.validate(voteDto);
    }
}
