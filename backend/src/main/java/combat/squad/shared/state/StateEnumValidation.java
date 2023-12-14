package combat.squad.shared.state;

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
@Constraint(validatedBy = StateEnumValidator.class)
public @interface StateEnumValidation {

    String message() default "must be any of {YES, IF_NEED_BE, NO}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
