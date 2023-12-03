package combat.squad.DtoTests;

import combat.squad.proposal.ProposalDto;
import combat.squad.proposal.DateValidator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.Instant;
import java.util.Date;

public class ProposalDtoTest {

    @Test
    public void testValidProposalDto() {

        Date startDate = Date.from(Instant.now().plusSeconds(3600));
        Date endDate = Date.from(startDate.toInstant().plusSeconds(3600));

        ProposalDto proposalDto = new ProposalDto(startDate, endDate);
        assertNotNull(proposalDto);
        assertEquals(startDate, proposalDto.startDate());
        assertEquals(endDate, proposalDto.endDate());
    }

    @Test
    public void testInvalidProposalDto() {
        Date startDate = Date.from(Instant.now().minusSeconds(3600)); // 1 hour ago
        Date endDate = Date.from(Instant.now().plusSeconds(3600)); // 1 hour from now

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new ProposalDto(startDate, endDate);
        });

        String expectedMessage = "Start date must be in the future";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

}
