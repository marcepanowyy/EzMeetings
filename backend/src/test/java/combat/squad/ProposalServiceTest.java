package combat.squad;

import combat.squad.event.EventEntity;
import combat.squad.event.EventRepository;
import combat.squad.proposal.ProposalDto;
import combat.squad.proposal.ProposalEntity;
import combat.squad.proposal.ProposalRepository;
import combat.squad.proposal.ProposalService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ProposalServiceTest {

    @Mock
    private ProposalRepository proposalRepository;

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private ProposalService proposalService;

    private static List<Date> startDates;
    private static List<Date> endDates;


    @BeforeAll
    static void setup() {
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
    public void testGetProposals() {
        EventEntity event = new EventEntity("Event Name", "Event Description", null, "Location", null, Arrays.asList());
        ProposalEntity proposal1 = new ProposalEntity(event, startDates.get(1), endDates.get(1));
        ProposalEntity proposal2 = new ProposalEntity(event, startDates.get(2), endDates.get(2));
        when(proposalRepository.findAll()).thenReturn(Arrays.asList(proposal1, proposal2));

        List<ProposalEntity> proposals = proposalService.getProposals();
        assertNotNull(proposals);
        assertEquals(2, proposals.size());
        verify(proposalRepository).findAll();
    }

    @Test
    public void testGetProposalById() {
        Long proposalId = 1L;
        EventEntity event = new EventEntity("Event Name", "Event Description", null, "Location", null, Arrays.asList());
        ProposalEntity proposal = new ProposalEntity(event, new Date(), new Date());
        when(proposalRepository.findById(proposalId)).thenReturn(Optional.of(proposal));

        ProposalEntity result = proposalService.getProposalById(proposalId);
        assertNotNull(result);
        verify(proposalRepository).findById(proposalId);
    }

    @Test
    public void testCreateNormalProposal() {
        Long eventId = 1L;
        EventEntity event = new EventEntity("Event Name", "Event Description", null, "Location", null, Arrays.asList());
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));

        ProposalDto proposalDto = new ProposalDto(startDates.get(1), endDates.get(1));
        ProposalEntity proposalEntity = new ProposalEntity(event, proposalDto.startDate(), proposalDto.endDate());
        when(proposalRepository.save(any(ProposalEntity.class))).thenReturn(proposalEntity);

        ProposalEntity result = proposalService.createProposal(proposalDto, eventId);
        assertNotNull(result);
        verify(proposalRepository).save(any(ProposalEntity.class));
        verify(eventRepository).findById(eventId);
    }

    @Test
    public void testCreateProposalWithSameDatesAsBefore() {
        Long eventId = 1L;
        Date startDate = startDates.get(1);
        Date endDate = endDates.get(1);
        EventEntity event = new EventEntity("Event Name", "Event Description", null, "Location", null, Arrays.asList());
        ProposalEntity existingProposal = new ProposalEntity(event, startDate, endDate);
        event.setProposals(List.of(existingProposal));
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        when(proposalRepository.save(any(ProposalEntity.class))).thenReturn(existingProposal);
        ProposalDto newProposalDto = new ProposalDto(startDate, endDate);
        assertThrows(IllegalArgumentException.class, () -> {
            proposalService.createProposal(newProposalDto, eventId);
        });
        verify(proposalRepository, never()).save(any(ProposalEntity.class));
    }

    @Test
    public void testDeleteProposal() {
        Long proposalId = 1L;
        proposalService.deleteProposal(proposalId);
        verify(proposalRepository).deleteById(proposalId);
    }
}
