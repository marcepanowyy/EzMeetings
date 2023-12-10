package combat.squad.event;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/event")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public List<EventRo> getEvents() {
        return this.eventService.getEvents();
    }

    @PostMapping
    public EventRo createEvent(@RequestBody EventDto eventDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return this.eventService.createEvent(authentication.getName(), eventDto);
    }

    @GetMapping("{id}")
    public EventRo getEventDetailsById(@PathVariable("id") UUID eventId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return this.eventService.getEventDetails(authentication.getName(), eventId);
    }

    @GetMapping("/all")
    public List<EventRo> getAllUserEvents() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return this.eventService.getAllUserEvents(authentication.getName());
    }

    @GetMapping("/created")
    public List<EventRo> getCreatedUserEvents() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return this.eventService.getCreatedUserEvents(authentication.getName());
    }

    @PostMapping("/participate/{id}")
    public EventRo participateInEvent(@PathVariable("id") UUID eventId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return this.eventService.participateInEvent(authentication.getName(), eventId);
    }


}
