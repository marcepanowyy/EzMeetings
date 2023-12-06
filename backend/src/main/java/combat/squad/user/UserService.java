package combat.squad.user;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

// add pagination in the future :)

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserRo> getAllUsers() {
        List<UserEntity> userEntities = userRepository.findAll();

        return userEntities.stream()
                .map(userEntity -> userEntity.toUserRo(false))
                .collect(Collectors.toList());
    }

    public UserEntity getUserById(UUID id) {
        return this.userRepository.findById(id).orElseThrow();
    }

    public UserRo createUser(UserDto userDto) {

        String email = userDto.email();

        Optional<UserEntity> existingUser = Optional.ofNullable(userRepository.findByEmail(email));

        if (existingUser.isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "User with email " + email + " already exists"
            );
        }

        UserEntity userEntity = new UserEntity(
                userDto.email(),
                userDto.password()
        );

        userRepository.save(userEntity);

        return userEntity.toUserRo(true);

    }

//    public UserEntity updateUser(UserEntity userEntity) {
//        return this.userRepository.save(userEntity);
//    }

//    public void deleteUser(Long id) {
//        this.userRepository.deleteById(id);
//    }

}
