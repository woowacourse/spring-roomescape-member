package roomescape.service.dto;

public record LoginRequest(
        String password,
        String email
) {
}
