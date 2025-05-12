package roomescape.dto.auth;

public record LoginCheckResponse(
        String name
) {

    public static LoginCheckResponse from(final String name) {
        return new LoginCheckResponse(name);
    }
}
