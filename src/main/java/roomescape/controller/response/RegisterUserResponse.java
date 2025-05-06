package roomescape.controller.response;

import roomescape.service.result.RegisterUserResult;

public record RegisterUserResponse(Long id, String email, String password, String name) {

    public static RegisterUserResponse from(RegisterUserResult registerUserResult) {
        return new RegisterUserResponse(registerUserResult.id(), registerUserResult.email(), registerUserResult.password(), registerUserResult.name());
    }
}
