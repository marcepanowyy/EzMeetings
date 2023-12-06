package combat.squad.user;

import combat.squad.event.EventEntity;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import java.util.Optional;

public record UserRo(
        UUID id,
        String email,
        List<EventEntity> eventList,
        Optional<String> token,
        Date created
) {
}