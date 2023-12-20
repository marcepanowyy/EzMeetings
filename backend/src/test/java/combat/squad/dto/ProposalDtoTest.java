package combat.squad.dto;

import combat.squad.proposal.ProposalDto;
import org.junit.jupiter.api.Test;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Date;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProposalDtoTest {

    @Test
    void testValidProposalDto() {

        ProposalDto proposalDto = new ProposalDto(new Date());

        Set<ConstraintViolation<ProposalDto>> violations = validate(proposalDto);
        assertEquals(1, violations.size());
    }

    @Test
    void testInvalidProposalDtoNullStartDate() {

        ProposalDto proposalDto = new ProposalDto(null);

        Set<ConstraintViolation<ProposalDto>> violations = validate(proposalDto);
        assertEquals(1, violations.size());
    }

    @Test
    void testInvalidProposalDtoPastStartDate() {

        Date pastStartDate = new Date(System.currentTimeMillis() - 100000);
        ProposalDto proposalDto = new ProposalDto(pastStartDate);

        Set<ConstraintViolation<ProposalDto>> violations = validate(proposalDto);
        assertEquals(1, violations.size());
    }

    private Set<ConstraintViolation<ProposalDto>> validate(ProposalDto proposalDto) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        return validator.validate(proposalDto);
    }
}
