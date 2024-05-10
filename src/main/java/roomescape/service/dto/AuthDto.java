package roomescape.service.dto;

import roomescape.controller.request.LoginRequest;

public class AuthDto {

    private final String email;

    public AuthDto(String email) {
        this.email = email;
    }

    public static AuthDto from(LoginRequest loginRequest) {
        return new AuthDto(loginRequest.getEmail());
    }

    public String getEmail() {
        return email;
    }
}
