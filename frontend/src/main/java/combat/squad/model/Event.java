package combat.squad.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
public class Event {
    private Long id;
    private String name;
    private String description;
    private String finalDate;
    private String location;
    private Long creatorId;

    @JsonProperty("eventProposals")
    private List<ProposalDate> eventProposals;
    public Event() {
    }

    public Event(Long id,String name, String description, String finalDate, String location, Long creatorId, List<ProposalDate> eventProposals) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.finalDate = finalDate;
        this.location = location;
        this.creatorId = creatorId;
        this.eventProposals = eventProposals;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getFinalDate() {
        return finalDate;
    }

    public String getLocation() {
        return location;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    @Override
    public String toString() {
        return "Event{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public Long getId() {
        return id;
    }
}
