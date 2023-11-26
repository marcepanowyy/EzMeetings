package combat.squad;
import combat.squad.event.EventEntity;
import combat.squad.event.EventRepository;
import combat.squad.proposal.ProposalDto;
import combat.squad.proposal.ProposalEntity;
import combat.squad.proposal.ProposalRepository;
import combat.squad.proposal.ProposalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;


import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@SpringBootTest
public class ProposalServiceTest {

    @Mock
    private ProposalRepository proposalRepository;

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private ProposalService proposalService;

    @BeforeEach
    public void setUp() {

    }

    @Test
    public void testGetProposals() {
        ProposalEntity proposal1 = new ProposalEntity();
        ProposalEntity proposal2 = new ProposalEntity();
        when(proposalRepository.findAll()).thenReturn(Arrays.asList(proposal1, proposal2));

        List<ProposalEntity> proposals = proposalService.getProposals();
        assertNotNull(proposals);
        assertEquals(2, proposals.size());
        verify(proposalRepository).findAll();
    }

    @Test
    public void testGetProposalById() {
        Long proposalId = 1L;
        ProposalEntity proposal = new ProposalEntity();
        when(proposalRepository.findById(proposalId)).thenReturn(Optional.of(proposal));

        ProposalEntity result = proposalService.getProposalById(proposalId);
        assertNotNull(result);
        verify(proposalRepository).findById(proposalId);
    }

    @Test
    public void testCreateProposal() {
        Long eventId = 1L;
        EventEntity event = new EventEntity();
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));

        ProposalDto proposalDto = new ProposalDto(
                new Date(),
                new Date()
        );
        ProposalEntity proposalEntity = new ProposalEntity();
        when(proposalRepository.save(any(ProposalEntity.class))).thenReturn(proposalEntity);

        ProposalEntity result = proposalService.createProposal(proposalDto, eventId);
        assertNotNull(result);
        verify(proposalRepository).save(any(ProposalEntity.class));
        verify(eventRepository).findById(eventId);
    }

    @Test
    public void testUpdateProposal() {
        ProposalEntity proposal = new ProposalEntity();
        when(proposalRepository.save(proposal)).thenReturn(proposal);

        ProposalEntity result = proposalService.updateProposal(proposal);
        assertNotNull(result);
        verify(proposalRepository).save(proposal);
    }

    @Test
    public void testDeleteProposal() {
        Long proposalId = 1L;
        proposalService.deleteProposal(proposalId);
        verify(proposalRepository).deleteById(proposalId);
    }
}
