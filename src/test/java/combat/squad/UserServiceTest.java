package combat.squad;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import combat.squad.user.UserDto;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import combat.squad.user.UserEntity;
import combat.squad.user.UserRepository;
import combat.squad.user.UserService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@SpringBootTest
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void testGetUsers() {

        UserEntity user1 = new UserEntity("nickname1");
        UserEntity user2 = new UserEntity("nickname2");
        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<UserEntity> users = userService.getUsers();
        assertEquals(2, users.size());
        verify(userRepository).findAll();
    }

    @Test
    public void testCreateUser() {
        UserDto userDto = new UserDto("nickname");
        userService.createUser(userDto);
        ArgumentCaptor<UserEntity> userEntityArgumentCaptor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userRepository).save(userEntityArgumentCaptor.capture());
        UserEntity capturedUserEntity = userEntityArgumentCaptor.getValue();
        assertEquals("nickname", capturedUserEntity.getNickname());
    }

    @Test
    public void testGetUserById() {
        UserEntity user = new UserEntity("nickname");
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        UserEntity userEntity = userService.getUserById(1L);
        assertEquals("nickname", userEntity.getNickname());

    }



}
