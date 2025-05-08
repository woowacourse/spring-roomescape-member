package roomescape.application.dto;

public record LoginRequest(
        String password,
        String email
) {
}
