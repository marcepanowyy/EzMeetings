package combat.squad.validator;

import combat.squad.shared.state.State;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StateEnumValidatorTest {

    @Test
    void testValidState() {

        State state = State.YES;

        Set<ConstraintViolation<State>> violations = validate(state);
        assertEquals(0, violations.size());
    }

    @Test
    void testInvalidState() {

        assertThrows(IllegalArgumentException.class, () -> {
            State state = State.valueOf("INVALID_STATE");
        });

    }

    private Set<ConstraintViolation<State>> validate(State state) {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        return validator.validate(state);
    }
}