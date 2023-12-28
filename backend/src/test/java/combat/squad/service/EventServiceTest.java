package combat.squad.service;

import combat.squad.auth.UserEntity;
import combat.squad.auth.UserRepository;
import combat.squad.event.EventEntity;
import combat.squad.event.EventRepository;
import combat.squad.event.EventRo;
import combat.squad.event.EventService;
import combat.squad.proposal.ProposalEntity;
import combat.squad.shared.state.State;
import combat.squad.vote.VoteEntity;
import org.junit.jupiter.api.BeforeEach;
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
public class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private EventService eventService;

    static UserEntity user1;
    static UserEntity user2;
    static UserEntity user3;

    static EventEntity event1;
    static EventEntity event2;

    static ProposalEntity proposal1;
    static ProposalEntity proposal2;
    static ProposalEntity proposal3;
    static ProposalEntity proposal4;
    static ProposalEntity proposal5;

    static VoteEntity user1Vote1;
    static VoteEntity user1Vote2;
    static VoteEntity user1Vote5;
    static VoteEntity user2Vote1;
    static VoteEntity user2Vote2;
    static VoteEntity user2Vote4;
    static VoteEntity user3Vote4;

    @BeforeEach
    public void setUp() {

        user1 = new UserEntity(
                "user1@example.com",
                "password"
        );

        user2 = new UserEntity(
                "user2@example.com",
                "password"
        );

        user3 = new UserEntity(
                "user3@example.com",
                "password"
        );

        event1 = new EventEntity(
                "name1",
                "description1",
                "location1",
                user1,
                new ArrayList<>()
        );

        event2 = new EventEntity(
                "name2",
                "description2",
                "location2",
                user2,
                new ArrayList<>()
        );

        proposal1 = new ProposalEntity(
                event1,
                new Date(System.currentTimeMillis() + 1000000)
        );

        proposal2 = new ProposalEntity(
                event1,
                new Date(System.currentTimeMillis() + 2000000)
        );

        proposal3 = new ProposalEntity(
                event1,
                new Date(System.currentTimeMillis() + 3000000)
        );

        proposal4 = new ProposalEntity(
                event2,
                new Date(System.currentTimeMillis() + 4000000)
        );

        proposal5 = new ProposalEntity(
                event2,
                new Date(System.currentTimeMillis() + 5000000)
        );

        user1Vote1 = new VoteEntity(
                user1,
                proposal1,
                State.YES
        );

        user1Vote2 = new VoteEntity(
                user1,
                proposal2,
                State.NO
        );

        user1Vote5 = new VoteEntity(
                user1,
                proposal5,
                State.IF_NEED_BE
        );

        user2Vote1 = new VoteEntity(
                user2,
                proposal1,
                State.YES
        );

        user2Vote2 = new VoteEntity(
                user2,
                proposal2,
                State.IF_NEED_BE
        );

        user2Vote4 = new VoteEntity(
                user2,
                proposal4,
                State.NO
        );

        user3Vote4 = new VoteEntity(
                user2,
                proposal4,
                State.NO
        );

        user1.setId(UUID.randomUUID());
        user2.setId(UUID.randomUUID());
        user3.setId(UUID.randomUUID());

        event1.setId(UUID.randomUUID());
        event2.setId(UUID.randomUUID());

        proposal1.setId(UUID.randomUUID());
        proposal2.setId(UUID.randomUUID());
        proposal3.setId(UUID.randomUUID());

        user1Vote1.setId(UUID.randomUUID());
        user1Vote2.setId(UUID.randomUUID());
        user1Vote5.setId(UUID.randomUUID());
        user2Vote1.setId(UUID.randomUUID());
        user2Vote2.setId(UUID.randomUUID());
        user2Vote4.setId(UUID.randomUUID());

        user1.setCreatedEvents(List.of(event1));
        user2.setCreatedEvents(List.of(event2));

        user1.setEvents(List.of(event1, event2));
        user2.setEvents(List.of(event1, event2));
        user3.setEvents(List.of(event2));

        event1.setParticipants(List.of(user1, user2));
        event2.setParticipants(List.of(user1, user2, user3));

        event1.setEventProposals(List.of(proposal1, proposal2, proposal3));
        event2.setEventProposals(List.of(proposal4, proposal5));

        event2.setFinalProposal(proposal5);

        proposal1.setVotes(List.of(user1Vote1, user2Vote1));
        proposal2.setVotes(List.of(user1Vote2, user2Vote2));
        proposal3.setVotes(new ArrayList<>());
        proposal4.setVotes(List.of(user2Vote4, user3Vote4));
        proposal5.setVotes(List.of(user1Vote5));

    }

    @Test
    public void testGetEvents() {

        List<EventEntity> sampleEvents = List.of(event1, event2);

        when(eventRepository.findAll()).thenReturn(sampleEvents);

        List<EventRo> result = eventService.getEvents();

        verify(eventRepository, times(1)).findAll();

        assertEquals(sampleEvents.size(), result.size());

        assertEquals(user1.getId(), result.get(0).creatorId().get());
        assertEquals(user1.getEmail(), result.get(0).creatorEmail().get());
        assertEquals(user2.getId(), result.get(1).creatorId().get());
        assertEquals(user2.getEmail(), result.get(1).creatorEmail().get());

        for (int i = 0; i < sampleEvents.size(); i++) {
            assertEquals(sampleEvents.get(i).getName(), result.get(i).name());
            assertEquals(sampleEvents.get(i).getDescription(), result.get(i).description());
            assertEquals(sampleEvents.get(i).getLocation(), result.get(i).location());

        }
    }

    @Test
    public void testCheckProposalHasNoVotes() {

        assertDoesNotThrow(() -> eventService.checkProposalHasNoVotes(proposal3));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            eventService.checkProposalHasNoVotes(proposal1);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Proposal has votes, cannot be modified", exception.getReason());

    }

    @Test
    public void testCheckProposalBelongsToEvent(){

        assertDoesNotThrow(() -> eventService.checkProposalBelongsToEvent(proposal1, event1));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            eventService.checkProposalBelongsToEvent(proposal1, event2);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Proposal does not belong to this event", exception.getReason());

    }

    @Test
    public void testGetUserByExistingEmail(){

            when(userRepository.findByEmail(user1.getEmail())).thenReturn(Optional.of(user1));

            UserEntity result = eventService.getUserByEmail(user1.getEmail());

            verify(userRepository, times(1)).findByEmail(user1.getEmail());

            assertEquals(user1.getId(), result.getId());
            assertEquals(user1.getEmail(), result.getEmail());
            assertEquals(user1.getPassword(), result.getPassword());
            assertEquals(user1.getCreated(), result.getCreated());
            assertEquals(user1.getRole(), result.getRole());
            assertEquals(user1.getCreatedEvents(), result.getCreatedEvents());
            assertEquals(user1.getEvents(), result.getEvents());

    }

    @Test
    public void testGetUserByNonExistingEmail(){

            String nonExistentEmail = "email";

            when(userRepository.findByEmail(nonExistentEmail)).thenReturn(Optional.empty());

            ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
                eventService.getUserByEmail(nonExistentEmail);
            });

            assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
            assertEquals("User not found", exception.getReason());

            verify(userRepository, times(1)).findByEmail(nonExistentEmail);

    }

    @Test
    public void testGetEventByExistingId(){

        when(eventRepository.findById(event1.getId())).thenReturn(Optional.of(event1));

        EventEntity result = eventService.getEventById(event1.getId());

        verify(eventRepository, times(1)).findById(event1.getId());

        assertEquals(event1.getId(), result.getId());
        assertEquals(event1.getName(), result.getName());
        assertEquals(event1.getDescription(), result.getDescription());
        assertEquals(event1.getLocation(), result.getLocation());
        assertEquals(event1.getCreator(), result.getCreator());
        assertEquals(event1.getCreated(), result.getCreated());
        assertEquals(event1.getParticipants(), result.getParticipants());
        assertEquals(event1.getEventProposals(), result.getEventProposals());
        assertEquals(event1.getFinalProposal(), result.getFinalProposal());

    }

    @Test
    public void testGetEventByNonExistingId(){

        UUID nonExistentId = UUID.randomUUID();

        when(eventRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            eventService.getEventById(nonExistentId);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Event not found", exception.getReason());

        verify(eventRepository, times(1)).findById(nonExistentId);

    }
}
