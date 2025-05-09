package roomescape.dto.response;

public record LoginCheckResponse(
    String name
) {
    public static LoginCheckResponse from(String name) {
        return new LoginCheckResponse(name);
    }
}
