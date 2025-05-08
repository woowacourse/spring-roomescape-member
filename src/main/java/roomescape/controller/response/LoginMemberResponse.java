package roomescape.controller.response;

import roomescape.service.result.LoginMemberResult;

public record LoginMemberResponse(Long id, String name, String email) {

    public static LoginMemberResponse from(LoginMemberResult loginMemberResult) {
        return new LoginMemberResponse(loginMemberResult.id(), loginMemberResult.name(), loginMemberResult.email());
    }
}
