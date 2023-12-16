package combat.squad.vote;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

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

    @PostMapping("{id}")
    public List<VoteRo> vote(@PathVariable("id") UUID eventId, @RequestBody List<VoteDto> voteDtos) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return this.voteService.vote(authentication.getName(), eventId, voteDtos);
    }


}
