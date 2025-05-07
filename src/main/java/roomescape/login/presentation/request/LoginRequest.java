package roomescape.login.presentation.request;

public record LoginRequest(
        String email,
        String password
) {
}
