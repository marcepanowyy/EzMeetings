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

    @GetMapping // only admin can access this endpoint
    public List<EventEntity> getEvents() {
        return this.eventService.getEvents();
    }

    @GetMapping("{id}") // only logged in users can access this endpoint
    public EventRo getEventById(@PathVariable("id") UUID eventId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return this.eventService.getEventDetails(authentication.getName(), eventId);
    }

    @GetMapping("/user") // only logged in users can access this endpoint
    public List<EventRo> getEventsByUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return this.eventService.getEventsByUser(authentication.getName());
    }

    @PostMapping ("/user")// only logged in users can access this endpoint
    public EventRo createEvent(@RequestBody EventDto eventDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return this.eventService.createEvent(authentication.getName(), eventDto);
    }

}
