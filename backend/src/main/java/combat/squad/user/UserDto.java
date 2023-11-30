package combat.squad.user;

public record UserDto(String nickname) {

    @Override
    public String nickname() {
        return nickname;
    }
}
