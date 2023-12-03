package combat.squad;

import static org.junit.jupiter.api.Assertions.*;

import combat.squad.proposal.DateValidator;
import org.junit.jupiter.api.Test;
import java.time.Instant;
import java.util.Date;

public class DateValidatorTest {

    @Test
    public void testValidDates() {
        Date startDate = Date.from(Instant.now().plusSeconds(3600));
        Date endDate = Date.from(startDate.toInstant().plusSeconds(3600));
        assertDoesNotThrow(() -> DateValidator.validateDates(startDate, endDate));
    }

    @Test
    public void testNullDates() {
        assertThrows(IllegalArgumentException.class, () -> DateValidator.validateDates(null, null));
    }

    @Test
    public void testStartDateInThePast() {
        Date startDate = Date.from(Instant.now().minusSeconds(3600));
        Date endDate = Date.from(Instant.now().plusSeconds(7200));
        assertThrows(IllegalArgumentException.class, () -> DateValidator.validateDates(startDate, endDate));
    }

    @Test
    public void testStartDateAfterEndDate() {
        Date startDate = Date.from(Instant.now().plusSeconds(7200));
        Date endDate = Date.from(Instant.now().plusSeconds(3600));
        assertThrows(IllegalArgumentException.class, () -> DateValidator.validateDates(startDate, endDate));
    }
    @Test
    public void testDurationLessThanOneHour() {
        Date startDate = Date.from(Instant.now().plusSeconds(1800));
        Date endDate = Date.from(Instant.now().plusSeconds(3600));
        assertThrows(IllegalArgumentException.class, () -> DateValidator.validateDates(startDate, endDate));
    }
}
