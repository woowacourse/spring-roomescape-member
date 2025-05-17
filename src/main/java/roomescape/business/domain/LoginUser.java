package roomescape.business.domain;

public record LoginUser(
        Long id, String name, Role role
) {

    public static LoginUser unknown() {
        return new LoginUser(0L, "unknown", Role.UNKNOWN);
    }

    public static LoginUser from(final User user) {
        return new LoginUser(
                user.getId(),
                user.getName(),
                user.getRole()
        );
    }
}
