package roomescape.auth.dto.request;

public record CreateTokenServiceRequest(String email, String password) {

    public static CreateTokenServiceRequest from(final LoginRequest request) {
        return new CreateTokenServiceRequest(request.email(), request.password());
    }
}
