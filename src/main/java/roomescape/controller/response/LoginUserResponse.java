package roomescape.controller.response;

import roomescape.service.result.LoginUserResult;

public record LoginUserResponse(Long id, String name, String email) {

    public static LoginUserResponse from(LoginUserResult loginUserResult) {
        return new LoginUserResponse(loginUserResult.id(), loginUserResult.name(), loginUserResult.email());
    }
}
