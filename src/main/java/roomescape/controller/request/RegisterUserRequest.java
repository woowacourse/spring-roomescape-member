package roomescape.controller.request;

import roomescape.service.param.RegisterUserParam;

public record RegisterUserRequest(String email, String password, String name) {
    public RegisterUserParam toServiceParam() {
        return new RegisterUserParam(email, password, name);
    }
}
