package roomescape.controller.request;

import roomescape.service.param.LoginUserParam;

public record LoginUserRequest(String email, String password) {

    public LoginUserParam toServiceParam() {
        return new LoginUserParam(email, password);
    }
}
