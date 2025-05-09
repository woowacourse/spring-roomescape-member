package roomescape.auth.service.dto;

import roomescape.auth.controller.dto.LoginRequest;

public record CreateTokenServiceRequest(String email, String password) {

    public static CreateTokenServiceRequest from(final LoginRequest request) {
        return new CreateTokenServiceRequest(request.email(), request.password());
    }
}
