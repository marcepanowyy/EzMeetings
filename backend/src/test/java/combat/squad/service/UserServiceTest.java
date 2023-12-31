package combat.squad.service;

import combat.squad.auth.*;
import combat.squad.auth.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void testGetAllUsers() {

        UserEntity user1 = new UserEntity("user1@example.com", "password1");
        UserEntity user2 = new UserEntity("user2@example.com", "password2");
        List<UserEntity> sampleUsers = List.of(user1, user2);

        when(userRepository.findAll()).thenReturn(sampleUsers);

        List<UserRo> result = userService.getAllUsers();

        verify(userRepository, times(1)).findAll();

        assertEquals(sampleUsers.size(), result.size());

        for (int i = 0; i < sampleUsers.size(); i++) {
            assertEquals(sampleUsers.get(i).getEmail(), result.get(i).email());
        }
    }

    @Test
    public void testGetUserByIdUserExists() {

        UUID userId = UUID.randomUUID();
        String userEmail = "existent@example.com";
        String userPassword = "correctPassword";

        UserEntity mockUser = new UserEntity(userEmail, userPassword);
        mockUser.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        UserEntity retrievedUser = userService.getUserById(userId);

        assertEquals(mockUser, retrievedUser);

        verify(userRepository, times(1)).findById(userId);

        assertEquals(userEmail, retrievedUser.getEmail());
        assertTrue(retrievedUser.comparePassword(userPassword));

    }

    @Test
    public void testGetUserByIdUserNotFound() {

        UUID userId = UUID.randomUUID();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> userService.getUserById(userId));

        verify(userRepository, times(1)).findById(userId);

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("User not found", exception.getReason());

    }

//    @Test
//    public void testRegisterNewUser() {
//
//        UserDto newUserDto = new UserDto("user@example.com", "correctPassword");
//
//        when(userRepository.findByEmail(newUserDto.email())).thenReturn(Optional.empty());
//        when(userRepository.save(any(UserEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
//
//        UserRo registeredUser = userService.register(newUserDto);
//
//        verify(userRepository, times(1)).findByEmail(newUserDto.email());
//        verify(userRepository, times(1)).save(any(UserEntity.class));
//
//        assertEquals(newUserDto.email(), registeredUser.email());
//
//    }

    @Test
    public void testRegisterExistingUser() {

        UserDto existingUserDto = new UserDto("existent@example.com", "correctPassword");

        when(userRepository.findByEmail(existingUserDto.email())).thenReturn(Optional.of(new UserEntity()));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> userService.register(existingUserDto));

        verify(userRepository, times(1)).findByEmail(existingUserDto.email());
        verify(userRepository, never()).save(any(UserEntity.class));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("User with email " + existingUserDto.email() + " already exists", exception.getReason());

    }

//    @Test
//    public void testLoginValidUser() {
//
//        UserDto validUserDto = new UserDto("existent@example.com", "correctPassword");
//
//        UserEntity mockUser = new UserEntity(validUserDto.email(), validUserDto.password());
//        when(userRepository.findByEmail(validUserDto.email())).thenReturn(Optional.of(mockUser));
//
//        UserRo loggedInUser = userService.login(validUserDto);
//
//        verify(userRepository, times(1)).findByEmail(validUserDto.email());
//
//        assertEquals(validUserDto.email(), loggedInUser.email());
//    }

    @Test
    public void testLoginInvalidUser() {

        UserDto invalidUserDto = new UserDto("nonexistent@example.com", "wrongPassword");

        when(userRepository.findByEmail(invalidUserDto.email())).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> userService.login(invalidUserDto));

        verify(userRepository, times(1)).findByEmail(invalidUserDto.email());

        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatus());
        assertEquals("Invalid email or password", exception.getReason());

    }

    @Test
    public void testLoginInvalidPassword() {

        UserDto validUserDto = new UserDto("user1@example.com", "correctPassword");

        UserEntity mockUser = new UserEntity(validUserDto.email(), "differentPassword");
        when(userRepository.findByEmail(validUserDto.email())).thenReturn(Optional.of(mockUser));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> userService.login(validUserDto));

        verify(userRepository, times(1)).findByEmail(validUserDto.email());

        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatus());
        assertEquals("Invalid email or password", exception.getReason());
    }


    @Test
    public void testLoadUserByUsernameUserExists() {

        String userEmail = "existent@example.com";
        UserEntity mockUser = new UserEntity(userEmail, "correctPassword");

        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(mockUser));

        UserDetails userDetails = userService.loadUserByUsername(userEmail);

        verify(userRepository, times(1)).findByEmail(userEmail);

        assertEquals(userEmail, userDetails.getUsername());

    }

    @Test
    public void testLoadUserByUsernameUserNotFound() {

        String userEmail = "nonexistent@example.com";

        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class, () -> userService.loadUserByUsername(userEmail));

        verify(userRepository, times(1)).findByEmail(userEmail);

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("User not found", exception.getReason());

    }
}
