package combat.squad;

import combat.squad.event.EventDto;
import combat.squad.event.EventEntity;
import combat.squad.event.EventRepository;
import combat.squad.event.EventService;


import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import combat.squad.proposal.ProposalDto;
import combat.squad.proposal.ProposalEntity;
import combat.squad.proposal.ProposalService;
import combat.squad.user.UserEntity;
import combat.squad.user.UserService;

@SpringBootTest
public class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private UserService userService;

    @Mock
    private ProposalService proposalService;

    @InjectMocks
    private EventService eventService;

    @BeforeEach
    public void setUp() {

    }

    @Test
    public void testGetEvents() {
        EventEntity event1 = new EventEntity();
        EventEntity event2 = new EventEntity();
        when(eventRepository.findAll()).thenReturn(Arrays.asList(event1, event2));

        List<EventEntity> events = eventService.getEvents();
        assertNotNull(events);
        assertEquals(2, events.size());
        verify(eventRepository).findAll();
    }

    @Test
    public void testGetEventById() {
        Long eventId = 1L;
        EventEntity event = new EventEntity();
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));

        EventEntity result = eventService.getEventById(eventId);
        assertNotNull(result);
        verify(eventRepository).findById(eventId);
    }

    @Test
    public void testCreateEvent() {
       //TODO
    }

    @Test
    public void testUpdateEvent() {
        EventEntity event = new EventEntity();
        when(eventRepository.save(event)).thenReturn(event);

        EventEntity result = eventService.updateEvent(event);
        assertNotNull(result);
        verify(eventRepository).save(event);
    }

    @Test
    public void testDeleteEvent() {
        Long eventId = 1L;
        eventService.deleteEvent(eventId);
        verify(eventRepository).deleteById(eventId);
    }
}
