package roomescape.dto;

public record SignUpRequest(
        String name,
        String email,
        String password) {
}
