package roomescape.presentation.api.auth;

import roomescape.application.auth.dto.LoginParam;

public record LoginRequest(String email, String password) {
    public LoginParam toServiceParam() {
        return new LoginParam(email, password);
    }
}
