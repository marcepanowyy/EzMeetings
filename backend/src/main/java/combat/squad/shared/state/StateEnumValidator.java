package combat.squad.shared.state;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class StateEnumValidator implements ConstraintValidator<StateEnumValidation, State> {

    @Override
    public boolean isValid(State value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        return value == State.YES || value == State.IF_NEED_BE || value == State.NO;
    }
}
