package roomescape.business.domain;

public record LoginUser(
        Long id, String name, Role role
) {

    public static LoginUser from(final User user) {
        return new LoginUser(
                user.getId(),
                user.getName(),
                user.getRole()
        );
    }
}
