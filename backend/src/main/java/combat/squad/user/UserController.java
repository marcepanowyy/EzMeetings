package combat.squad.user;

import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserRo> getAllUsers() {
        return this.userService.getAllUsers();
    }

    @GetMapping("{id}")
    public UserEntity getUserById(@PathVariable("id") UUID id) {
        return this.userService.getUserById(id);
    }

    @PostMapping
    public UserRo register(@Valid @RequestBody UserDto userDto) {
        return this.userService.createUser(userDto);
    }

//    @GetMapping("{id}/events")
//    public List<EventEntity> getEventsByUserId(@PathVariable("id") UUID id) {
//        return this.userService.getUserById(id).getEvents();
//    }

//    @PutMapping
//    public UserEntity updateUser(@RequestBody UserEntity userEntity) {
//        return this.userService.updateUser(userEntity);
//    }
//
//    @DeleteMapping("{id}")
//    public void deleteUser(@PathVariable("id") Long id){
//        this.userService.deleteUser(id);
//    }


}
