package combat.squad.service;

import combat.squad.auth.UserEntity;
import combat.squad.auth.UserRepository;
import combat.squad.event.EventEntity;
import combat.squad.event.EventRepository;
import combat.squad.event.EventRo;
import combat.squad.event.EventService;
import combat.squad.proposal.ProposalEntity;
import combat.squad.proposal.ProposalService;
import combat.squad.shared.state.State;
import combat.squad.vote.VoteEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    private ProposalEntity proposalEntity;

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

//    @Test
//    public void testDeleteEvent(){
//
//        when(eventRepository.findById(event1.getId())).thenReturn(Optional.ofNullable(event1));
//        when(userRepository.findByEmail(user1.getEmail())).thenReturn(Optional.ofNullable(user1));
//        doNothing().when(eventRepository).delete(event1);
//
//        eventService.deleteEvent(user1.getEmail(), event1.getId());
//
//        verify(eventRepository, times(1)).findById(event1.getId());
//        verify(eventRepository, times(1)).delete(event1);
//
//        assertEquals(0, user1.getCreatedEvents().size());
//        assertEquals(1, user1.getEvents().size());
//
//    }

//    @Test
//    public void testGetEventDetails(){
//
//        when(eventRepository.findById(event1.getId())).thenReturn(Optional.ofNullable(event1));
//        when(eventRepository.findById(event2.getId())).thenReturn(Optional.ofNullable(event2));
//        when(userRepository.findByEmail(user1.getEmail())).thenReturn(Optional.ofNullable(user1));
//        when(userRepository.findByEmail(user2.getEmail())).thenReturn(Optional.ofNullable(user2));
//
//        event1.setEventProposals(List.of(proposal1, proposal2, proposal3));
//
//        EventRo result1 = eventService.getEventDetails(user1.getEmail(), event1.getId());
//        EventRo result2 = eventService.getEventDetails(user2.getEmail(), event2.getId());
//
//        verify(eventRepository, times(1)).findById(event1.getId());
//        verify(eventRepository, times(1)).findById(event2.getId());
//        verify(userRepository, times(1)).findByEmail(user1.getEmail());
//        verify(userRepository, times(1)).findByEmail(user2.getEmail());
//
//        assertEquals(event1.getId(), result1.id());
//        assertEquals(event1.getName(), result1.name());
//        assertEquals(event1.getDescription(), result1.description());
//        assertEquals(event1.getLocation(), result1.location());
//        assertEquals(event1.getCreator().getId(), result1.creatorId().get());
//        assertEquals(event1.getCreator().getEmail(), result1.creatorEmail().get());
//
//        System.out.println(result1.eventProposals());
//        System.out.println(result2.eventProposals());
//
////        assertEquals(event1.getFinalProposal().getId(), result1.finalProposalId());
//
//    }



}
