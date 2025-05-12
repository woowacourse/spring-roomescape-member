package roomescape.dto.response;

public record LoginCheckResponse(
    Long id,
    String name
) {
    public static LoginCheckResponse from(Long memberId, String name) {
        return new LoginCheckResponse(memberId, name);
    }
}
