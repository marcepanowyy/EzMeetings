package combat.squad.vote;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vote")
public class VoteController {

    private final VoteService voteService;

    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @GetMapping
    public List<VoteEntity> getVotes() {
        return this.voteService.getVotes();
    }

    @GetMapping("{id}")
    public VoteEntity getVoteById(@PathVariable("id") Long id) {
        return this.voteService.getVoteById(id);
    }

    @PostMapping
    public VoteEntity createVote(@RequestBody VoteDto voteDTO) {
        return this.voteService.createVote(voteDTO);
    }

//    @PutMapping
//    public VoteEntity updateVote(@RequestBody VoteEntity voteEntity) {
//        return this.voteService.updateVote(voteEntity);
//    }

//    @DeleteMapping("{id}")
//    public void deleteVote(@PathVariable("id") Long id){
//        this.voteService.deleteVote(id);
//    }

}
