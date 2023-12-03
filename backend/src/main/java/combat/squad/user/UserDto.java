package combat.squad.user;
import javax.validation.constraints.NotBlank;
public record UserDto(
        @NotBlank(message = "Nickname cannot be blank")
        String nickname
) {

    @Override
    public String nickname() {
        return nickname;
    }
}

