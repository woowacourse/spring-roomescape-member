package roomescape.dto.response;

public record LoginCheckResponse(
    String name
) {
    public static LoginCheckResponse of(String name) {
        return new LoginCheckResponse(name);
    }
}
