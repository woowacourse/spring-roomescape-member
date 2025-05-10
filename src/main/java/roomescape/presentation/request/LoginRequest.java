package roomescape.presentation.request;

import roomescape.application.param.LoginParam;

public record LoginRequest(String email, String password) {
    public LoginParam toServiceParam() {
        return new LoginParam(email, password);
    }
}
