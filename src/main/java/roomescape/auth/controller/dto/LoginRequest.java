package roomescape.auth.controller.dto;

import roomescape.auth.service.dto.CreateTokenServiceRequest;

public record LoginRequest(String email, String password) {

    public CreateTokenServiceRequest toCreateTokenServiceRequest() {
        return new CreateTokenServiceRequest(email, password);
    }
}
