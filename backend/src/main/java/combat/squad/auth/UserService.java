package combat.squad.auth;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

// add pagination in the future :)

@Service
public class UserService implements UserDetailsService {

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

    public UserRo register(UserDto userDto) {

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

    public UserRo login(UserDto userDto) {

        String email = userDto.email();
        String password = userDto.password();

        UserEntity user = userRepository.findByEmail(email);

        if (user == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid email or password"
            );
        }

        if(!user.comparePassword(password)) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid email or password"
            );
        }

        return user.toUserRo(true);

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity user = userRepository.findByEmail(username);

        if(user == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "User not found");
        }

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.getAuthorities()
        );

    }


}
