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

    @GetMapping("{id}")
    public EventRo getEventDetailsById(@PathVariable("id") UUID eventId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return this.eventService.getEventDetails(authentication.getName(), eventId);
    }

    @GetMapping("/user")
    public List<EventRo> getEventsByUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return this.eventService.getEventsByUser(authentication.getName());
    }

    @PostMapping ("/user")
    public EventRo createEvent(@RequestBody EventDto eventDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return this.eventService.createEvent(authentication.getName(), eventDto);
    }

}
