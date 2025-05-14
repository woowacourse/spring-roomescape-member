package roomescape.dto.request;

public record LoginRequest(
        String password,
        String email
) {
}
