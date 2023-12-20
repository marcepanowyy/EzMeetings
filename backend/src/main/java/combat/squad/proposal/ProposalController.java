package combat.squad.proposal;


import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/proposal")
public class ProposalController {

    private final ProposalService proposalService;

    public ProposalController(ProposalService proposalService) {
        this.proposalService = proposalService;
    }

    @GetMapping
    public List<ProposalRo> getProposals() {
        return this.proposalService.getProposals();
    }

    // check all the votes for a proposal

//    @GetMapping("{id}")
//    public ProposalRo getProposalById(@PathVariable("id") UUID id) {
//        return this.proposalService.getProposalById(id);
//    }
//
//    @PostMapping("{eventId}")
//    public ProposalEntity createProposal(@RequestBody ProposalDto proposalDTO, @PathVariable("eventId") UUID eventId) {
//        return this.proposalService.createProposal(proposalDTO, eventId);
//    }

//    @DeleteMapping("{id}")
//    public void deleteProposal(@PathVariable("id") UUID id){
//        this.proposalService.deleteProposal(id);
//    }

}
