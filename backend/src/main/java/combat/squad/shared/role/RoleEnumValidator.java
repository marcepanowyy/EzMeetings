package combat.squad.shared.role;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class RoleEnumValidator implements ConstraintValidator<RoleEnumValidation, Role> {

    @Override
    public boolean isValid(Role value, ConstraintValidatorContext context) {

        if (value == null) {
            return true;
        }

        return value == Role.USER || value == Role.ADMIN;

    }
}
