package roomescape.auth.presentation.request;

public record LoginRequest(
        String email,
        String password
) {
}
