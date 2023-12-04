package combat.squad.DtoTests;

import combat.squad.event.EventDto;
import combat.squad.proposal.ProposalDto;
import org.junit.jupiter.api.*;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.ConstraintViolation;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.springframework.test.util.AssertionErrors.assertFalse;
import static org.springframework.test.util.AssertionErrors.assertTrue;


class EventDtoTest {

    private static LocalValidatorFactoryBean validator;
    private static List<Date> startDates;
    private static List<Date> endDates;

    @BeforeAll
    static void setup() {
        validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date startDate1 = null;
        Date endDate1 = null;
        Date startDate2 = null;
        Date endDate2 = null;
        Date startDate3 = null;
        Date endDate3 = null;
        try{
            startDate1 = simpleDateFormat.parse("2023-12-06 13:00");
            endDate1 = simpleDateFormat.parse("2023-12-06 14:00");
            startDate2 = simpleDateFormat.parse("2023-12-06 14:00");
            endDate2 = simpleDateFormat.parse("2023-12-06 15:00");
            startDate3 = simpleDateFormat.parse("2023-12-06 15:00");
            endDate3 = simpleDateFormat.parse("2023-12-06 16:00");
        }
        catch(Exception e){
            System.out.println(e);
        }
        startDates = List.of(startDate1, startDate2, startDate3);
        endDates = List.of(endDate1, endDate2, endDate3);
    }

    @Test
    void shouldTriggerNameValidationMessage() {
        EventDto eventWithBlankName = new EventDto("", "description", null, "location", 1L, List.of(
                new ProposalDto(startDates.get(0), endDates.get(0)),
                new ProposalDto(startDates.get(1), endDates.get(1)),
                new ProposalDto(startDates.get(2), endDates.get(2)
        )));
        Set<ConstraintViolation<EventDto>> violations = validator.validate(eventWithBlankName);

        assertTrue("Expected 'name cannot be blank' message",violations.stream().anyMatch(violation -> violation.getMessage().equals("Name cannot be blank")));
    }

    @Test
    void shouldTriggerDescriptionValidationMessage() {
        EventDto eventWithBlankDescription = new EventDto("name", "", null, "location", 1L, List.of(new ProposalDto(
                startDates.get(0), endDates.get(0)),
                new ProposalDto(startDates.get(1), endDates.get(1)),
                new ProposalDto(startDates.get(2), endDates.get(2)
        )));
        Set<ConstraintViolation<EventDto>> violations = validator.validate(eventWithBlankDescription);

        assertTrue("Expected 'description cannot be blank' message", violations.stream().anyMatch(violation -> violation.getMessage().equals("Description cannot be blank")));
    }

    @Test
    void shouldTriggerLocationValidationMessage() {
        EventDto eventWithBlankLocation = new EventDto("name", "description", null, "", 1L, List.of(new ProposalDto(
                startDates.get(0), endDates.get(0)),
                new ProposalDto(startDates.get(1), endDates.get(1)),
                new ProposalDto(startDates.get(2), endDates.get(2)
        )));
        Set<ConstraintViolation<EventDto>> violations = validator.validate(eventWithBlankLocation);
        assertTrue( "Expected 'location cannot be blank' message", violations.stream().anyMatch(violation -> violation.getMessage().equals("Location cannot be blank")));
    }

    @Test
    void shouldTriggerCreatorIdValidationMessage() {
        EventDto eventWithNullCreatorId = new EventDto("name", "description", null, "location", null, List.of(new ProposalDto(
                startDates.get(0), endDates.get(0)),
                new ProposalDto(startDates.get(1), endDates.get(1)),
                new ProposalDto(startDates.get(2), endDates.get(2)
        )));
        Set<ConstraintViolation<EventDto>> violations = validator.validate(eventWithNullCreatorId);
        assertTrue("Expected 'creator ID cannot be null' message", violations.stream().anyMatch(violation -> violation.getMessage().equals("Creator ID cannot be null")));
    }

    @Test
    void shouldTriggerEventProposalsValidationMessage() {
        EventDto eventWithEmptyProposals = new EventDto("name", "description", null, "location", 1L, List.of());
        Set<ConstraintViolation<EventDto>> violations = validator.validate(eventWithEmptyProposals);
        assertTrue( "Expected 'event proposals cannot be empty' message",violations.stream().anyMatch(violation -> violation.getMessage().equals("Event proposals cannot be empty")));
    }
}

