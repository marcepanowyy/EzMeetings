package combat.squad.shared.role;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({
        ElementType.METHOD,
        ElementType.FIELD,
        ElementType.ANNOTATION_TYPE,
        ElementType.CONSTRUCTOR,
        ElementType.PARAMETER,
        ElementType.TYPE_USE
})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = RoleEnumValidator.class)
public @interface RoleEnumValidation {

    String message() default "must be any of {USER, ADMIN}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
