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

import java.text.SimpleDateFormat;
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
        EventEntity event1 = new EventEntity(
                "name",
                "description",
                null,
                "location",
                null,
                new ArrayList<>()
        );
        EventEntity event2 = new EventEntity(
                "name",
                "description",
                null,
                "location",
                null,
                new ArrayList<>()
        );
        when(eventRepository.findAll()).thenReturn(Arrays.asList(event1, event2));

        List<EventEntity> events = eventService.getEvents();
        assertNotNull(events);
        assertEquals(2, events.size());
        verify(eventRepository).findAll();
    }

    @Test
    public void testGetEventById() {
        Long eventId = 1L;
        EventEntity event = new EventEntity(
                "name",
                "description",
                null,
                "location",
                null,
                new ArrayList<>()
        );
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));

        EventEntity result = eventService.getEventById(eventId);
        assertNotNull(result);
        verify(eventRepository).findById(eventId);
    }

    @Test
    public void testCreateEvent() {
        Long userId = 1L;
        UserEntity mockUser = new UserEntity("TestUser");
        List<ProposalDto> proposals = new ArrayList<>();
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

        proposals.add(new ProposalDto(startDate1, endDate1));
        proposals.add(new ProposalDto(startDate2, endDate2));
        proposals.add(new ProposalDto(startDate3, endDate3));
        EventDto eventDto = new EventDto("Event Name", "Description", null, "Location", userId,proposals);

        when(userService.getUserById(userId)).thenReturn(mockUser);
        when(eventRepository.save(any(EventEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        EventEntity createdEvent = eventService.createEvent(eventDto);

        assertNotNull(createdEvent);
        assertEquals("Event Name", createdEvent.getName());
        assertEquals("Description", createdEvent.getDescription());
        assertEquals("Location", createdEvent.getLocation());
    }
    @Test
    public void testUpdateEvent() {
        EventEntity event = new EventEntity(
                "name",
                "description",
                null,
                "location",
                null,
                new ArrayList<>()
        );
        when(eventRepository.save(event)).thenReturn(event);
        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(event));
        Long eventId = 1L;
        EventDto eventDto = new EventDto("Event Name", "Description", null, "Location", 1L, new ArrayList<>());
        EventEntity result = eventService.updateEvent(eventId,eventDto);
        assertNotNull(result);
        assertEquals("Event Name", result.getName());
        verify(eventRepository).save(event);
    }

    @Test
    public void testDeleteEvent() {
        Long eventId = 1L;
        eventService.deleteEvent(eventId);
        verify(eventRepository).deleteById(eventId);
    }
}
