package combat.squad.dto;

import combat.squad.event.EventDto;
import combat.squad.proposal.ProposalDto;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EventDtoTest {

    @Test
    void testValidEventDto() {

        EventDto eventDto = new EventDto(
                "Valid Name",
                "This is a valid description",
                "Valid Location",
                List.of(new ProposalDto(new Date())));

        Set<ConstraintViolation<EventDto>> violations = validate(eventDto);
        assertEquals(0, violations.size());

    }

    @Test
    void testInvalidEventDtoBlankName() {

        EventDto eventDto = new EventDto(
                "",
                "This is a valid description",
                "Valid Location",
                List.of(new ProposalDto(new Date())));

        Set<ConstraintViolation<EventDto>> violations = validate(eventDto);
        assertEquals(2, violations.size());

    }

    @Test
    void testInvalidEventDtoShortDescription() {

        EventDto eventDto = new EventDto(
                "Valid Name",
                "Short",
                "Valid Location",
                List.of(new ProposalDto(new Date())));

        Set<ConstraintViolation<EventDto>> violations = validate(eventDto);
        assertEquals(1, violations.size());
    }

    @Test
    void testInvalidEventDtoBlankLocation() {
        EventDto eventDto = new EventDto(
                "Valid Name",
                "This is a valid description",
                "",
                List.of(new ProposalDto(new Date())));

        Set<ConstraintViolation<EventDto>> violations = validate(eventDto);
        assertEquals(2, violations.size());
    }

    @Test
    void testInvalidEventDtoEmptyEventProposals() {
        EventDto eventDto = new EventDto(
                "Valid Name",
                "This is a valid description",
                "Valid Location",
                Collections.emptyList());

        Set<ConstraintViolation<EventDto>> violations = validate(eventDto);
        assertEquals(1, violations.size());
    }

    private Set<ConstraintViolation<EventDto>> validate(EventDto eventDto) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        return validator.validate(eventDto);
    }
}
