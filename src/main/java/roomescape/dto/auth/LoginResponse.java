package roomescape.dto.auth;

public record LoginResponse(
        Long memberId,
        String accessToken
) {
}
