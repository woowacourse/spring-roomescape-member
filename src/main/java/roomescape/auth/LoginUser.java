package roomescape.auth;

public record LoginUser(
        String name
) {
    public static final String SESSION_KEY = "LOGIN_USER";
}
