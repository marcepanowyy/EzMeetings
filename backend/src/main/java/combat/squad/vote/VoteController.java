package combat.squad.vote;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/vote")
public class VoteController {

    private final VoteService voteService;

    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @GetMapping
    public List<VoteRo> getVotes() {
        return this.voteService.getVotes();
    }

    @PostMapping
    public List<VoteRo> vote(@RequestBody List<VoteDto> voteDtos) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return this.voteService.vote(authentication.getName(), voteDtos);
    }


}
