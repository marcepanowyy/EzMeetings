package combat.squad.service;

import combat.squad.auth.UserEntity;
import combat.squad.event.EventEntity;
import combat.squad.event.EventRepository;
import combat.squad.proposal.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class ProposalServiceTest {

    @Mock
    private ProposalRepository proposalRepository;

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private ProposalService proposalService;

    static UserEntity user;

    static EventEntity event;

    static ProposalDto proposalDto;

    @BeforeAll
    public static void setUp() {

        proposalDto = new ProposalDto(
                new Date(System.currentTimeMillis() + 1000000)
        );

        user = new UserEntity(
                "user@example.com",
                "password"
        );

        event = new EventEntity(
                "name",
                "description",
                "location",
                user,
                new ArrayList<>()
        );

    }

    @Test
    public void testCreateProposal(){

        UUID eventId = UUID.randomUUID();

        when(eventRepository.findById(eventId)).thenReturn(Optional.ofNullable(event));

        when(proposalRepository.save(any(ProposalEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ProposalEntity result = proposalService.createProposal(proposalDto, eventId);

        verify(eventRepository, times(1)).findById(eventId);
        verify(proposalRepository, times(1)).save(any(ProposalEntity.class));

        assertNotNull(result);
        assertEquals(event, result.getEvent());
        assertEquals(proposalDto.startDate(), result.getStartDate());

    }

    @Test
    public void testGetProposals(){

        ProposalEntity proposal1 = new ProposalEntity(
                event,
                proposalDto.startDate()
        );

        ProposalEntity proposal2 = new ProposalEntity(
                event,
                new Date(System.currentTimeMillis() + 2000000)
        );

        List<ProposalEntity> proposals = new ArrayList<>();

        proposals.add(proposal1);
        proposals.add(proposal2);

        when(proposalRepository.findAll()).thenReturn(proposals);

        List<ProposalRo> result = proposalService.getProposals();

        verify(proposalRepository, times(1)).findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(proposal1.getStartDate(), result.get(0).startDate());
        assertEquals(proposal2.getStartDate(), result.get(1).startDate());

    }

    @Test
    public void testGetProposalByExistingId(){

        UUID proposalId = UUID.randomUUID();

        ProposalEntity proposal = new ProposalEntity(
                event,
                proposalDto.startDate()
        );

        when(proposalRepository.findById(proposalId)).thenReturn(Optional.of(proposal));

        ProposalEntity result = proposalService.getProposalById(proposalId);

        verify(proposalRepository, times(1)).findById(proposalId);

        assertNotNull(result);
        assertEquals(proposal, result);

    }

    @Test
    public void testGetProposalByNonExistingId(){

        UUID proposalId = UUID.randomUUID();

        when(proposalRepository.findById(proposalId)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            proposalService.getProposalById(proposalId);
        });

        verify(proposalRepository, times(1)).findById(proposalId);

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Proposal not found", exception.getReason());

    }

    @Test
    public void testDeleteExistingProposal(){

        UUID proposalId = UUID.randomUUID();

        ProposalEntity proposal = new ProposalEntity(
                event,
                proposalDto.startDate()
        );

        when(proposalRepository.findById(proposalId)).thenReturn(Optional.of(proposal));
        doNothing().when(proposalRepository).delete(proposal);

        proposalService.deleteProposal(proposalId);

        verify(proposalRepository, times(1)).findById(proposalId);
        verify(proposalRepository, times(1)).delete(proposal);

    }

    @Test
    public void testDeleteNonExistingProposal(){

        UUID proposalId = UUID.randomUUID();

        when(proposalRepository.findById(proposalId)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            proposalService.deleteProposal(proposalId);
        });

        verify(proposalRepository, times(1)).findById(proposalId);

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Proposal not found", exception.getReason());

    }
}