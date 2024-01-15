package combat.squad.service;

import combat.squad.auth.UserEntity;
import combat.squad.auth.UserRepository;
import combat.squad.event.*;
import combat.squad.proposal.*;
import combat.squad.shared.state.State;
import combat.squad.vote.VoteEntity;
import combat.squad.vote.VoteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
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

    @Mock
    private ProposalService proposalService;

    @Mock
    private ProposalRepository proposalRepository;

    @Mock
    private VoteService voteService;

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

        user1.setCreatedEvents(new ArrayList<>(List.of(event1)));
        user2.setCreatedEvents(new ArrayList<>(List.of(event2)));

        user1.setEvents(new ArrayList<>(List.of(event1, event2)));
        user2.setEvents(new ArrayList<>(List.of(event1, event2)));
        user3.setEvents(new ArrayList<>(List.of(event2)));

        event1.setParticipants(new ArrayList<>(List.of(user1, user2)));
        event2.setParticipants(new ArrayList<>(List.of(user1, user2, user3)));

        event1.setEventProposals(new ArrayList<>(List.of(proposal1, proposal2, proposal3)));
        event2.setEventProposals(new ArrayList<>(List.of(proposal4, proposal5)));

        event2.setFinalProposal(proposal5);

        proposal1.setVotes(new ArrayList<>(List.of(user1Vote1, user2Vote1)));
        proposal2.setVotes(new ArrayList<>(List.of(user1Vote2, user2Vote2)));
        proposal3.setVotes(new ArrayList<>());
        proposal4.setVotes(new ArrayList<>(List.of(user2Vote4, user3Vote4)));
        proposal5.setVotes(new ArrayList<>(List.of(user1Vote5)));

    }

    @Test
    public void testGetEvents() {

        List<EventEntity> sampleEvents = List.of(event1, event2);

        when(eventRepository.findAll()).thenReturn(sampleEvents);

        List<EventRo> result = eventService.getEvents();

        verify(eventRepository, times(1)).findAll();

        assertEquals(sampleEvents.size(), result.size());

        assertEquals(user1.getId(), result.get(0).creatorId().orElse(null));
        assertEquals(user1.getEmail(), result.get(0).creatorEmail().orElse(null));
        assertEquals(user2.getId(), result.get(1).creatorId().orElse(null));
        assertEquals(user2.getEmail(), result.get(1).creatorEmail().orElse(null));

        for (int i = 0; i < sampleEvents.size(); i++) {
            assertEquals(sampleEvents.get(i).getName(), result.get(i).name());
            assertEquals(sampleEvents.get(i).getDescription(), result.get(i).description());
            assertEquals(sampleEvents.get(i).getLocation(), result.get(i).location());

        }
    }

    @Test
    public void testDeleteEventByCreator(){

        when(userRepository.findByEmail(user1.getEmail())).thenReturn(Optional.of(user1));
        when(eventRepository.findById(event1.getId())).thenReturn(Optional.of(event1));
        doNothing().when(eventRepository).delete(event1);

        this.eventService.deleteEvent(user1.getUsername(), event1.getId());

        verify(userRepository, times(1)).findByEmail(user1.getEmail());
        verify(eventRepository, times(1)).findById(event1.getId());
        verify(eventRepository, times(1)).delete(event1);

        assertFalse(user1.getEvents().contains(event1));
        assertFalse(user1.getCreatedEvents().contains(event1));

    }

    @Test
    public void testDeleteEventByNonCreator(){

        when(userRepository.findByEmail(user2.getEmail())).thenReturn(Optional.of(user2));
        when(eventRepository.findById(event1.getId())).thenReturn(Optional.of(event1));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            this.eventService.deleteEvent(user2.getUsername(), event1.getId());
        });

        verify(userRepository, times(1)).findByEmail(user2.getEmail());
        verify(eventRepository, times(1)).findById(event1.getId());

        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
        assertEquals("User is not the creator of this event", exception.getReason());

    }

    @Test
    public void testGetEventDetails(){

        when(userRepository.findByEmail(user1.getEmail())).thenReturn(Optional.of(user1));
        when(eventRepository.findById(event1.getId())).thenReturn(Optional.of(event1));

        EventRo result = eventService.getEventDetails(user1.getEmail(), event1.getId());

        verify(userRepository, times(1)).findByEmail(user1.getEmail());
        verify(eventRepository, times(1)).findById(event1.getId());

        assertEquals(event1.getId(), result.id());
        assertEquals(event1.getName(), result.name());
        assertEquals(event1.getDescription(), result.description());
        assertEquals(event1.getLocation(), result.location());

        List<ProposalRo> proposalRosResult = result.eventProposals().orElse(null);
        assertNotNull(proposalRosResult);
        assertEquals(event1.getEventProposals().size(), proposalRosResult.size());

        assertEquals(user1.getId(), result.creatorId().orElse(null));
        assertEquals(user1.getEmail(), result.creatorEmail().orElse(null));

        assertNull(result.finalProposalId().orElse(null));

    }

    @Test
    public void testGetAllUserEvents(){

        when(userRepository.findByEmail(user1.getEmail())).thenReturn(Optional.of(user1));

        List<EventRo> result = eventService.getAllUserEvents(user1.getEmail());

        verify(userRepository, times(1)).findByEmail(user1.getEmail());

        assertEquals(user1.getEvents().size(), result.size());
        assertEquals(user1.getEvents().get(0).getId(), result.get(0).id());
        assertEquals(user1.getEvents().get(1).getId(), result.get(1).id());

    }

    @Test
    public void testGetUserCreatedEvents(){

        when(userRepository.findByEmail(user1.getEmail())).thenReturn(Optional.of(user1));

        List<EventRo> result = eventService.getCreatedUserEvents(user1.getEmail());

        verify(userRepository, times(1)).findByEmail(user1.getEmail());

        assertEquals(user1.getCreatedEvents().size(), result.size());
        assertEquals(user1.getCreatedEvents().get(0).getId(), result.get(0).id());

    }

    @Test
    public void testCreateEvent(){

        ProposalDto proposalDto1 = new ProposalDto(
                new Date(System.currentTimeMillis() + 1000000)
        );

        ProposalDto proposalDto2 = new ProposalDto(
                new Date(System.currentTimeMillis() + 1000000)
        );

        EventDto eventDto = new EventDto(
                "name",
                "description",
                "location",
                List.of(proposalDto1, proposalDto2)
        );

        when(userRepository.findByEmail(user1.getEmail())).thenReturn(Optional.of(user1));
        when(eventRepository.save(any(EventEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        EventRo result = this.eventService.createEvent(user1.getEmail(), eventDto);

        assertEquals(eventDto.name(), result.name());
        assertEquals(eventDto.description(), result.description());
        assertEquals(eventDto.location(), result.location());

        List<ProposalRo> proposalRosResult = result.eventProposals().orElse(null);
        assertNotNull(proposalRosResult);

        assertEquals(eventDto.eventProposals().size(), proposalRosResult.size());

    }

    @Test
    public void testCreateEventWithNoProposals(){

        EventDto eventDto = new EventDto(
                "name",
                "description",
                "location",
                new ArrayList<>()
        );

        when(userRepository.findByEmail(user1.getEmail())).thenReturn(Optional.of(user1));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            this.eventService.createEvent(user1.getEmail(), eventDto);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Event must have at least one proposal", exception.getReason());

    }

    @Test
    public void testParticipateInEventNonExistingUserEmail(){

        String nonExistingEmail = "email";

        when(userRepository.findByEmail(nonExistingEmail)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            eventService.participateInEvent(nonExistingEmail, event1.getId());
        });

        verify(userRepository, times(1)).findByEmail(nonExistingEmail);

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("User not found", exception.getReason());

    }

    @Test
    public void testParticipateInEventNonExistingEventId(){

        UUID nonExistingEventId = UUID.randomUUID();

        when(userRepository.findByEmail(user3.getEmail())).thenReturn(Optional.of(user3));
        when(eventRepository.findById(nonExistingEventId)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            eventService.participateInEvent(user3.getEmail(), nonExistingEventId);
        });

        verify(userRepository, times(1)).findByEmail(user3.getEmail());
        verify(eventRepository, times(1)).findById(nonExistingEventId);

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Event not found", exception.getReason());

    }

    @Test
    public void testParticipateInEventUserAlreadyParticipates(){

        when(userRepository.findByEmail(user1.getEmail())).thenReturn(Optional.of(user1));
        when(eventRepository.findById(event1.getId())).thenReturn(Optional.of(event1));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            eventService.participateInEvent(user1.getEmail(), event1.getId());
        });

        verify(userRepository, times(1)).findByEmail(user1.getEmail());
        verify(eventRepository, times(1)).findById(event1.getId());

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("User is already participating in this event", exception.getReason());

    }

    @Test
    public void testUpdateEvent(){

        ProposalDto proposalDto1 = new ProposalDto(
                proposal1.getStartDate()
        );

        ProposalDto proposalDto2 = new ProposalDto(
                proposal2.getStartDate()
        );

        EventDto eventDto = new EventDto(
                "name",
                "description",
                "location",
                List.of(proposalDto1, proposalDto2)
        );

        when(userRepository.findByEmail(user1.getEmail())).thenReturn(Optional.of(user1));
        when(eventRepository.findById(event1.getId())).thenReturn(Optional.of(event1));
        when(eventRepository.save(any(EventEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        EventRo result = this.eventService.updateEvent(user1.getEmail(), event1.getId(), eventDto);

        assertEquals(eventDto.name(), result.name());
        assertEquals(eventDto.description(), result.description());
        assertEquals(eventDto.location(), result.location());

        List<ProposalRo> proposalRosResult = result.eventProposals().orElse(null);
        assertNotNull(proposalRosResult);

        assertEquals(eventDto.eventProposals().size(), proposalRosResult.size());

    }

    @Test
    public void testUpdateEventNonExistingUserEmail(){

        String nonExistingEmail = "email";

        when(userRepository.findByEmail(nonExistingEmail)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            eventService.updateEvent(nonExistingEmail, event1.getId(), null);
        });

        verify(userRepository, times(1)).findByEmail(nonExistingEmail);
        verify(eventRepository, times(0)).findById(event1.getId());

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("User not found", exception.getReason());

    }

    @Test void testUpdateEventNonExistingEvent(){

        UUID nonExistingEventId = UUID.randomUUID();

        when(userRepository.findByEmail(user1.getEmail())).thenReturn(Optional.of(user1));
        when(eventRepository.findById(nonExistingEventId)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            eventService.updateEvent(user1.getEmail(), nonExistingEventId, null);
        });

        verify(userRepository, times(1)).findByEmail(user1.getEmail());
        verify(eventRepository, times(1)).findById(nonExistingEventId);

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Event not found", exception.getReason());

    }

    @Test void testUpdateEventNonExistingUserIsNotCreator(){

        when(userRepository.findByEmail(user2.getEmail())).thenReturn(Optional.of(user2));
        when(eventRepository.findById(event1.getId())).thenReturn(Optional.of(event1));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            eventService.updateEvent(user2.getEmail(), event1.getId(), null);
        });

        verify(userRepository, times(1)).findByEmail(user2.getEmail());
        verify(eventRepository, times(1)).findById(event1.getId());

        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
        assertEquals("User is not the creator of this event", exception.getReason());

    }

    @Test
    void testUpdateEventWithNoProposals(){

        EventDto eventDto = new EventDto(
                "name",
                "description",
                "location",
                new ArrayList<>()
        );

        when(userRepository.findByEmail(user1.getEmail())).thenReturn(Optional.of(user1));
        when(eventRepository.findById(event1.getId())).thenReturn(Optional.of(event1));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            eventService.updateEvent(user1.getEmail(), event1.getId(), eventDto);
        });

        verify(userRepository, times(1)).findByEmail(user1.getEmail());
        verify(eventRepository, times(1)).findById(event1.getId());

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Event must have at least one proposal", exception.getReason());

    }

    @Test
    void testUpdateEventDeleteProposalToHaveVotes(){

        ProposalDto proposalDto1 = new ProposalDto(
                proposal1.getStartDate()
        );

        // no proposal 2 which has votes

        EventDto eventDto = new EventDto(
                "name",
                "description",
                "location",
                List.of(proposalDto1)
        );

        when(userRepository.findByEmail(user1.getEmail())).thenReturn(Optional.of(user1));
        when(eventRepository.findById(event1.getId())).thenReturn(Optional.of(event1));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            eventService.updateEvent(user1.getEmail(), event1.getId(), eventDto);
        });

        verify(userRepository, times(1)).findByEmail(user1.getEmail());
        verify(eventRepository, times(1)).findById(event1.getId());

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Proposal has votes, cannot be modified", exception.getReason());

    }

}
