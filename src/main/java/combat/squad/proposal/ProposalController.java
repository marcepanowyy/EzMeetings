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

    @PostMapping
    public ProposalEntity createProposal(@RequestBody ProposalEntity proposalEntity) {
        return this.proposalService.createProposal(proposalEntity);
    }

    @PutMapping
    public ProposalEntity updateProposal(@RequestBody ProposalEntity proposalEntity) {
        return this.proposalService.updateProposal(proposalEntity);
    }

    @DeleteMapping("{id}")
    public void deleteProposal(@PathVariable("id") Long id){
        this.proposalService.deleteProposal(id);
    }

}
