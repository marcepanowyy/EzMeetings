package combat.squad.auth;

import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public List<UserRo> getAllUsers() {
        return this.userService.getAllUsers();
    }

    @GetMapping("{id}")
    public UserEntity getUserById(@PathVariable("id") UUID id) {
        return this.userService.getUserById(id);
    }

    @PostMapping("/register")
    public UserRo register(@Valid @RequestBody UserDto userDto) {
        return this.userService.register(userDto);
    }

    @PostMapping("/login")
    public UserRo login(@RequestBody UserDto userDto) {   // don't have to check validation here
        return this.userService.login(userDto);
    }

}
