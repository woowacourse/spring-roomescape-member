package roomescape.dto.response;

public record LoginResponse(
    String token
) {
    public static LoginResponse from(String token) {
        return new LoginResponse(token);
    }
}
