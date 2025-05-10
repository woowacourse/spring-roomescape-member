package roomescape.auth.sign.application.dto;

public record SignInServiceRequest(String email,
                                   String password) {
}
