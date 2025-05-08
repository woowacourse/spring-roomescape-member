package roomescape.dto.request;

public record LoginRequest(
        // TODO: email 형식 검증
        String email,
        String password
) {
}
