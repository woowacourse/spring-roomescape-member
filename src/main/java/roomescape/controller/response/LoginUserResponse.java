package roomescape.controller.response;

import roomescape.service.result.LoginMemberResult;

public record LoginUserResponse(Long id, String name, String email) {

    public static LoginUserResponse from(LoginMemberResult loginMemberResult) {
        return new LoginUserResponse(loginMemberResult.id(), loginMemberResult.name(), loginMemberResult.email());
    }
}
