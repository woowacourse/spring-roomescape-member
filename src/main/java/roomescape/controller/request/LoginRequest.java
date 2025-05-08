package roomescape.controller.request;

import roomescape.service.param.LoginParam;

public record LoginRequest(String email, String password) {
    public LoginParam toServiceParam() {
        return new LoginParam(email, password);
    }
}
