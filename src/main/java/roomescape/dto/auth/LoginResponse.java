package roomescape.dto.auth;

public record LoginResponse(
        String token
) {

    public static LoginResponse from(final String token) {
        return new LoginResponse(token);
    }
}
