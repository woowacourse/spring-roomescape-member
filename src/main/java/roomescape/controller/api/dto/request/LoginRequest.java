package roomescape.controller.api.dto.request;

import roomescape.service.dto.request.LoginServiceRequest;

public record LoginRequest(
        String email,
        String password
) {

    public LoginServiceRequest toServiceRequest() {
        return new LoginServiceRequest(email, password);
    }
}
