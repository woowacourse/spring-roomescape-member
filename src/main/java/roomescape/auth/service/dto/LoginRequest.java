package roomescape.auth.service.dto;

public record LoginRequest(
        String password,
        String email
) {
}
