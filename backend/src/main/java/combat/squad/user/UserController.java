package combat.squad.user;

import combat.squad.event.EventEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserEntity> getUsers() {
        return this.userService.getUsers();
    }

    @GetMapping("{id}")
    public UserEntity getUserById(@PathVariable("id") Long id) {
        return this.userService.getUserById(id);
    }

    @GetMapping("{id}/events")
    public List<EventEntity> getEventsByUserId(@PathVariable("id") Long id) {
        return this.userService.getUserById(id).getEvents();
    }

    @PostMapping
    public UserEntity createUser(@RequestBody UserDto userDto) {
        return this.userService.createUser(userDto);
    }

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
