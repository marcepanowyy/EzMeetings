package combat.squad.validator;

import combat.squad.shared.role.Role;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RoleEnumValidatorTest {

    @Test
    void testValidRole() {

        Role role = Role.USER;

        Set<ConstraintViolation<Role>> violations = validate(role);
        assertEquals(0, violations.size());
    }

    @Test
    void testInvalidRole() {
        assertThrows(IllegalArgumentException.class, () -> {
            Role role = Role.valueOf("INVALID_ROLE");
        });
    }

    private Set<ConstraintViolation<Role>> validate(Role role) {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        return validator.validate(role);
    }

}
