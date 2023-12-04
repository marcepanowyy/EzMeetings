package combat.squad.proposal;


import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/proposal")
public class ProposalController {

    private final ProposalService proposalService;

    public ProposalController(ProposalService proposalService) {
        this.proposalService = proposalService;
    }

    @GetMapping
    public List<ProposalEntity> getProposals() {
        return this.proposalService.getProposals();
    }

    @GetMapping("{id}")
    public ProposalEntity getProposalById(@PathVariable("id") Long id) {
        return this.proposalService.getProposalById(id);
    }

    @PostMapping("{eventId}")
    public ProposalEntity createProposal(@RequestBody ProposalDto proposalDTO, @PathVariable("eventId") Long eventId) {
        return this.proposalService.createProposal(proposalDTO, eventId);
    }

    @DeleteMapping("{id}")
    public void deleteProposal(@PathVariable("id") Long id){
        this.proposalService.deleteProposal(id);
    }

}
