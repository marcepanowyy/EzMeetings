package combat.squad.service;

import combat.squad.auth.UserEntity;
import combat.squad.auth.UserRepository;
import combat.squad.event.EventEntity;
import combat.squad.event.EventRepository;
import combat.squad.proposal.ProposalEntity;
import combat.squad.proposal.ProposalRepository;
import combat.squad.shared.state.State;
import combat.squad.vote.*;
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
public class VoteServiceTest {

    @Mock
    private VoteRepository voteRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProposalRepository proposalRepository;

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private VoteService voteService;

    static UserEntity user;

    static EventEntity event;

    static ProposalEntity proposal1;
    static ProposalEntity proposal2;
    static ProposalEntity proposal3;

    @BeforeAll
    public static void setUp() {

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

        proposal1 = new ProposalEntity(
                event,
                new Date(System.currentTimeMillis() + 1000000)
        );

        proposal2 = new ProposalEntity(
                event,
                new Date(System.currentTimeMillis() + 2000000)
        );

        proposal3 = new ProposalEntity(
                event,
                new Date(System.currentTimeMillis() + 3000000)
        );

        user.setId(UUID.randomUUID());

        event.setId(UUID.randomUUID());
        event.getEventProposals().addAll(List.of(proposal1, proposal2, proposal3));

        proposal1.setId(UUID.randomUUID());
        proposal2.setId(UUID.randomUUID());
        proposal3.setId(UUID.randomUUID());

    }

    @Test
    public void testGetVotes(){

        VoteEntity vote1 = new VoteEntity(
                user,
                proposal1,
                State.YES
        );

        VoteEntity vote2 = new VoteEntity(
                user,
                proposal2,
                State.NO
        );

        VoteEntity vote3 = new VoteEntity(
                user,
                proposal3,
                State.YES
        );

        vote1.setId(UUID.randomUUID());
        vote2.setId(UUID.randomUUID());
        vote3.setId(UUID.randomUUID());

        List<VoteEntity> sampleVotes = List.of(vote1, vote2, vote3);

        when(voteRepository.findAll()).thenReturn(sampleVotes);

        List<VoteRo> result = voteService.getVotes();

        verify(voteRepository, times(1)).findAll();

        assertEquals(sampleVotes.size(), result.size());

        for (int i = 0; i < sampleVotes.size(); i++) {
            assertEquals(sampleVotes.get(i).getId(), result.get(i).voteId());
            assertEquals(sampleVotes.get(i).getVoter().getId(), result.get(i).voterId().get());
            assertEquals(sampleVotes.get(i).getVoter().getEmail(), result.get(i).voterEmail().get());
            assertEquals(sampleVotes.get(i).getState(), result.get(i).state());
        }
    }

    @Test
    public void testCreateVotePresent(){

        VoteDto voteDto = new VoteDto(proposal1.getId(), State.NO);

        VoteEntity existingVote = new VoteEntity(user, proposal1, State.YES);
        existingVote.setId(UUID.randomUUID());
        proposal1.getVotes().add(existingVote);

        when(proposalRepository.findById(proposal1.getId())).thenReturn(Optional.ofNullable(proposal1));
        when(voteRepository.findByVoterAndProposal(user, proposal1)).thenReturn(Optional.of(existingVote));
        when(voteRepository.save(any(VoteEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        VoteRo result = voteService.createVote(voteDto, user);

        verify(proposalRepository, times(1)).findById(proposal1.getId());
        verify(voteRepository, times(1)).findByVoterAndProposal(user, proposal1);
        verify(voteRepository, times(1)).save(any(VoteEntity.class));

        assertNotNull(result);
        assertEquals(existingVote.getId(), result.voteId());
        assertEquals(voteDto.state(), result.state());

    }

    @Test
    public void testCreateVoteNotPresent(){

        VoteDto voteDto = new VoteDto(proposal1.getId(), State.NO);

        when(proposalRepository.findById(proposal1.getId())).thenReturn(Optional.ofNullable(proposal1));
        when(voteRepository.findByVoterAndProposal(user, proposal1)).thenReturn(Optional.empty());
        when(voteRepository.save(any(VoteEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        VoteRo result = voteService.createVote(voteDto, user);

        verify(proposalRepository, times(1)).findById(proposal1.getId());
        verify(voteRepository, times(1)).findByVoterAndProposal(user, proposal1);
        verify(voteRepository, times(1)).save(any(VoteEntity.class));

        assertNotNull(result);
        assertEquals(voteDto.state(), result.state());

    }

    @Test
    public void testCreateVoteProposalNotFound(){

        VoteDto voteDto = new VoteDto(proposal1.getId(), State.NO);

        when(proposalRepository.findById(proposal1.getId())).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class, () -> voteService.createVote(voteDto, user));

        verify(proposalRepository, times(1)).findById(proposal1.getId());
        verify(voteRepository, times(0)).findByVoterAndProposal(user, proposal1);
        verify(voteRepository, times(0)).save(any(VoteEntity.class));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Proposal not found", exception.getReason());

    }

    @Test
    public void testVote(){

        proposal1.getVotes().clear();
        proposal2.getVotes().clear();
        proposal3.getVotes().clear();

        VoteEntity existingVote1 = new VoteEntity(user, proposal1, State.YES);
        existingVote1.setId(UUID.randomUUID());

        VoteEntity existingVote2 = new VoteEntity(user, proposal2, State.NO);
        existingVote2.setId(UUID.randomUUID());

        proposal1.getVotes().add(existingVote1);
        proposal2.getVotes().add(existingVote2);

        List<VoteDto> voteDtos = List.of(
                new VoteDto(proposal2.getId(), State.YES),
                new VoteDto(proposal3.getId(), State.IF_NEED_BE)
        );

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.ofNullable(user));
        when(eventRepository.findById(event.getId())).thenReturn(Optional.ofNullable(event));

        when(proposalRepository.findById(proposal1.getId())).thenReturn(Optional.ofNullable(proposal1));
        when(proposalRepository.findById(proposal2.getId())).thenReturn(Optional.ofNullable(proposal2));
        when(proposalRepository.findById(proposal3.getId())).thenReturn(Optional.ofNullable(proposal3));

        when(voteRepository.findByVoterAndProposal(user, proposal1)).thenReturn(Optional.of(existingVote1));
        when(voteRepository.findByVoterAndProposal(user, proposal2)).thenReturn(Optional.of(existingVote2));
        when(voteRepository.findByVoterAndProposal(user, proposal3)).thenReturn(Optional.empty());
        when(voteRepository.findById(existingVote1.getId())).thenReturn(Optional.of(existingVote1));

        doNothing().when(voteRepository).delete(existingVote1);
        when(voteRepository.save(any(VoteEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        List<VoteRo> result = voteService.vote(user.getEmail(), event.getId(), voteDtos);

        verify(userRepository, times(1)).findByEmail(user.getEmail());
        verify(eventRepository, times(1)).findById(event.getId());

        verify(proposalRepository, times(1)).findById(proposal1.getId());
        verify(proposalRepository, times(1)).findById(proposal2.getId());
        verify(proposalRepository, times(1)).findById(proposal3.getId());

        assertNotNull(result);
        assertEquals(2, result.size());

        assertEquals(existingVote2.getId(), result.get(0).voteId());

        for (int i = 0; i < result.size(); i++) {
            assertEquals(voteDtos.get(i).state(), result.get(i).state());
        }

    }

    @Test
    public void testDeleteVote(){

        proposal1.getVotes().clear();

        UUID voteId = UUID.randomUUID();

        VoteEntity vote = new VoteEntity(
                user,
                proposal1,
                State.YES
        );

        vote.setId(voteId);
        proposal1.getVotes().add(vote);

        when(proposalRepository.findById(proposal1.getId())).thenReturn(Optional.of(proposal1));
        when(voteRepository.findById(voteId)).thenReturn(Optional.of(vote));
        doNothing().when(voteRepository).delete(vote);

        voteService.deleteVote(proposal1.getId(), voteId);

        verify(voteRepository, times(1)).findById(voteId);
        verify(voteRepository, times(1)).delete(vote);

        assertEquals(0, proposal1.getVotes().size());

    }

    @Test
    public void testFindVoteByUserAndProposal() {

        UUID voteId = UUID.randomUUID();
        VoteEntity existingVote = new VoteEntity(user, proposal1, State.YES);
        existingVote.setId(voteId);
        proposal1.getVotes().add(existingVote);

        when(voteRepository.findByVoterAndProposal(user, proposal1)).thenReturn(Optional.of(existingVote));
        when(voteRepository.findByVoterAndProposal(user, proposal2)).thenReturn(Optional.empty());

        VoteEntity result1 = voteService.findVoteByUserAndProposal(user, proposal1);
        VoteEntity result2 = voteService.findVoteByUserAndProposal(user, proposal2);

        verify(voteRepository, times(1)).findByVoterAndProposal(user, proposal1);
        verify(voteRepository, times(1)).findByVoterAndProposal(user, proposal2);

        assertEquals(existingVote, result1);
        assertNull(result2);

    }

    @Test
    public void testGetVoteByExistingId(){

        UUID voteId = UUID.randomUUID();

        VoteEntity vote = new VoteEntity(
                user,
                proposal1,
                State.YES
        );

        when(voteRepository.findById(voteId)).thenReturn(Optional.of(vote));

        VoteEntity result = voteService.getVoteById(voteId);

        verify(voteRepository, times(1)).findById(voteId);

        assertNotNull(result);
        assertEquals(vote, result);

    }

    @Test
    public void testGetVoteByNonExistingId(){

        UUID voteId = UUID.randomUUID();

        when(voteRepository.findById(voteId)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            voteService.getVoteById(voteId);
        });

        verify(voteRepository, times(1)).findById(voteId);

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Vote not found", exception.getReason());

    }

    @Test
    public void testGetProposalByExistingId(){

        when(proposalRepository.findById(proposal1.getId())).thenReturn(Optional.of(proposal1));

        ProposalEntity result = voteService.getProposalById(proposal1.getId());

        verify(proposalRepository, times(1)).findById(proposal1.getId());

        assertNotNull(result);
        assertEquals(proposal1, result);

    }

    @Test
    public void testGetProposalByNonExistingId(){

        when(proposalRepository.findById(proposal1.getId())).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            voteService.getProposalById(proposal1.getId());
        });

        verify(proposalRepository, times(1)).findById(proposal1.getId());

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Proposal not found", exception.getReason());

    }

    @Test
    public void testGetUserByExistingEmail(){

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        UserEntity result = voteService.getUserByEmail(user.getEmail());

        verify(userRepository, times(1)).findByEmail(user.getEmail());

        assertNotNull(result);
        assertEquals(user, result);

    }

    @Test
    public void testGetUserByNonExistingEmail(){

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            voteService.getUserByEmail(user.getEmail());
        });

        verify(userRepository, times(1)).findByEmail(user.getEmail());

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("User not found", exception.getReason());

    }

    @Test
    public void testGetEventByExistingId(){

        when(eventRepository.findById(event.getId())).thenReturn(Optional.of(event));

        EventEntity result = voteService.getEventById(event.getId());

        verify(eventRepository, times(1)).findById(event.getId());

        assertNotNull(result);
        assertEquals(event, result);

    }

    @Test
    public void testGetEventByNonExistingId(){

        when(eventRepository.findById(event.getId())).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            voteService.getEventById(event.getId());
        });

        verify(eventRepository, times(1)).findById(event.getId());

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Event not found", exception.getReason());

    }

}