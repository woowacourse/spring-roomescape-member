package roomescape.auth.dto;

public record LoginResponse(
        Long memberId,
        String accessToken
) {
}
