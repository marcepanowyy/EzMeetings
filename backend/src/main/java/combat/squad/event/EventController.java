package combat.squad.event;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/event")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public List<EventEntity> getEvents() {
        return this.eventService.getEvents();
    }

    @GetMapping("{id}")
    public EventEntity getEventById(@PathVariable("id") Long id) {
        return this.eventService.getEventById(id);
    }

    @PostMapping
    public EventEntity createEvent(@RequestBody EventDto eventDto) {
        return this.eventService.createEvent(eventDto);
    }

//    @PutMapping
//    public EventEntity updateEvent(@RequestBody EventEntity eventEntity) {
//        return this.eventService.updateEvent(eventEntity);
//    }
//
//    @DeleteMapping("{id}")
//    public void deleteEvent(@PathVariable("id") Long id){
//        this.eventService.deleteEvent(id);
//    }

}
