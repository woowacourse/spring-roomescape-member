package roomescape.auth.dto.request;

public record TokenRequest(
        String email,
        String password
) {
}
