package combat.squad.dto;

import combat.squad.auth.UserDto;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserDtoTest {

    @Test
    void testValidUserDto() {

        UserDto userDto = new UserDto(
                "valid.email@example.com",
                "ValidPassword1!");

        Set<ConstraintViolation<UserDto>> violations = validate(userDto);
        assertEquals(0, violations.size());

    }

    @Test
    void testInvalidUserDtoBlankEmail() {

        UserDto userDto = new UserDto(
                "",
                "ValidPassword1!");

        Set<ConstraintViolation<UserDto>> violations = validate(userDto);
        assertEquals(1, violations.size());

    }

    @Test
    void testInvalidUserDtoInvalidEmail() {

        UserDto userDto = new UserDto(
                "invalid.email",
                "ValidPassword1!");

        Set<ConstraintViolation<UserDto>> violations = validate(userDto);
        assertEquals(1, violations.size());

    }

    @Test
    void testInvalidUserDtoBlankPassword() {

        UserDto userDto = new UserDto(
                "valid.email@example.com",
                "");

        Set<ConstraintViolation<UserDto>> violations = validate(userDto);
        assertEquals(5, violations.size());

    }

    @Test
    void testInvalidUserDtoShortPassword() {

        UserDto userDto = new UserDto(
                "valid.email@example.com",
                "Short1!");

        Set<ConstraintViolation<UserDto>> violations = validate(userDto);
        assertEquals(1, violations.size());

    }

    @Test
    void testInvalidUserDtoWeakPassword() {

        UserDto userDto = new UserDto(
                "valid.email@example.com",
                "weakpassword");

        Set<ConstraintViolation<UserDto>> violations = validate(userDto);
        assertEquals(3, violations.size());

    }

    private Set<ConstraintViolation<UserDto>> validate(UserDto userDto) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        return validator.validate(userDto);
    }
}
