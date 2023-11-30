package combat.squad;

import combat.squad.vote.VoteDto;
import combat.squad.vote.VoteEntity;
import combat.squad.vote.VoteRepository;
import combat.squad.vote.VoteService;


import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import combat.squad.proposal.ProposalEntity;
import combat.squad.proposal.ProposalService;
import combat.squad.user.UserEntity;
import combat.squad.user.UserService;

@SpringBootTest
public class VoteServiceTest {

    @Mock
    private VoteRepository voteRepository;

    @Mock
    private UserService userService;

    @Mock
    private ProposalService proposalService;

    @InjectMocks
    private VoteService voteService;

    @BeforeEach
    public void setUp() {
    }

    @Test
    public void testGetVotes() {
        VoteEntity vote1 = new VoteEntity();
        VoteEntity vote2 = new VoteEntity();
        when(voteRepository.findAll()).thenReturn(Arrays.asList(vote1, vote2));

        List<VoteEntity> votes = voteService.getVotes();
        assertNotNull(votes);
        assertEquals(2, votes.size());
        verify(voteRepository).findAll();
    }

    @Test
    public void testGetVoteById() {
        Long voteId = 1L;
        VoteEntity vote = new VoteEntity();
        when(voteRepository.findById(voteId)).thenReturn(Optional.of(vote));

        VoteEntity result = voteService.getVoteById(voteId);
        assertNotNull(result);
        verify(voteRepository).findById(voteId);
    }

    @Test
    public void testCreateVote() {
        Long voterId = 1L;
        Long proposalId = 1L;
        String state = "Agree";

        UserEntity voter = new UserEntity();
        ProposalEntity proposal = new ProposalEntity();
        VoteDto voteDto = new VoteDto(voterId, proposalId, state);

        when(userService.getUserById(voterId)).thenReturn(voter);
        when(proposalService.getProposalById(proposalId)).thenReturn(proposal);

        VoteEntity voteEntity = new VoteEntity(voter, proposal, state);
        when(voteRepository.save(any(VoteEntity.class))).thenReturn(voteEntity);

        VoteEntity result = voteService.createVote(voteDto);
        assertNotNull(result);
        verify(voteRepository).save(any(VoteEntity.class));
        verify(userService).getUserById(voterId);
        verify(proposalService).getProposalById(proposalId);
    }

    @Test
    public void testUpdateVote() {
        VoteEntity vote = new VoteEntity();
        when(voteRepository.save(vote)).thenReturn(vote);

        VoteEntity result = voteService.updateVote(vote);
        assertNotNull(result);
        verify(voteRepository).save(vote);
    }

    @Test
    public void testDeleteVote() {
        Long voteId = 1L;
        voteService.deleteVote(voteId);
        verify(voteRepository).deleteById(voteId);
    }
}

