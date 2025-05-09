package roomescape.dto.response;

public record UserLoginCheckResponse(
        String name
) {
    public static UserLoginCheckResponse from(String name) {
        return new UserLoginCheckResponse(name);
    }
}
