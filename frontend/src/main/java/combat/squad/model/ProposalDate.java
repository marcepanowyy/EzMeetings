package combat.squad.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)//TODO: add votes to class
public class ProposalDate {
    private Long id;
    private String startDate;
    private String endDate;
    public ProposalDate() {
    }

    public ProposalDate(Long id, String startDate, String endDate) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public Long getId() {
        return id;
    }
}
