package roomescape.controller.response;

import roomescape.service.result.RegisterMemberResult;

public record RegisterUserResponse(Long id, String email, String password, String name) {

    public static RegisterUserResponse from(RegisterMemberResult registerMemberResult) {
        return new RegisterUserResponse(registerMemberResult.id(), registerMemberResult.email(), registerMemberResult.password(), registerMemberResult.name());
    }
}
