package roomescape.dto;

public record SignupRequest(
        // TODO email 형식 검증
        String email,
        String name,
        String password
) {
}
