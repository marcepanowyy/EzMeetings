package combat.squad.proposal;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProposalService {

private final ProposalRepository proposalRepository;

    public ProposalService(ProposalRepository proposalRepository) {
        this.proposalRepository = proposalRepository;
    }

    public List<ProposalEntity> getProposals() {
        return this.proposalRepository.findAll();
    }

    public ProposalEntity getProposalById(Long id) {
        return this.proposalRepository.findById(id).orElseThrow();
    }

    public ProposalEntity createProposal(ProposalEntity proposalEntity) {
        return this.proposalRepository.save(proposalEntity);
    }

    public ProposalEntity updateProposal(ProposalEntity proposalEntity) {
        return this.proposalRepository.save(proposalEntity);
    }

    public void deleteProposal(Long id) {
        this.proposalRepository.deleteById(id);
    }

}
