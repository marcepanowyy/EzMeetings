package combat.squad.DtoTests;

import combat.squad.user.UserDto;
import org.junit.jupiter.api.*;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.ConstraintViolation;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserDtoTest {

    private static LocalValidatorFactoryBean validator;

    @BeforeAll
    static void setup() {
        validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();
    }

    @Test
    void shouldValidateUserDto() {
        UserDto validUser = new UserDto("nickname");
        Set<ConstraintViolation<UserDto>> violations = validator.validate(validUser);
        assertTrue(violations.isEmpty());

        UserDto invalidUser = new UserDto("");
        violations = validator.validate(invalidUser);
        assertFalse(violations.isEmpty());
    }
}

