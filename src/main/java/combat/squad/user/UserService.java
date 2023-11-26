package combat.squad.user;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserEntity> getUsers() {return this.userRepository.findAll();}

    public UserEntity getUserById(Long id) {
        return this.userRepository.findById(id).orElseThrow();
    }

    public UserEntity createUser(UserDto userDto) {

        // unique decorator on column nickname
        String nickname = userDto.nickname();

        UserEntity userEntity = new UserEntity(
                userDto.nickname()
        );
        return this.userRepository.save(userEntity);
    }

//    public UserEntity updateUser(UserEntity userEntity) {
//        return this.userRepository.save(userEntity);
//    }

//    public void deleteUser(Long id) {
//        this.userRepository.deleteById(id);
//    }

}
