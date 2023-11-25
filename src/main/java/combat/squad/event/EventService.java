package combat.squad.event;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public List<EventEntity> getEvents() {
        return this.eventRepository.findAll();
    }

    public EventEntity getEventById(Long id) {
        return this.eventRepository.findById(id).orElseThrow();
    }

    public EventEntity createEvent(EventEntity eventEntity) {
        return this.eventRepository.save(eventEntity);
    }

    public EventEntity updateEvent(EventEntity eventEntity) {
        return this.eventRepository.save(eventEntity);
    }

    public void deleteEvent(Long id) {
        this.eventRepository.deleteById(id);
    }

}
