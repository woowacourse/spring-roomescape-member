package roomescape.controller.dto.response;

import roomescape.service.dto.response.MemberSignUpResult;

public record MemberSignUpResponse(String name) {
    public static MemberSignUpResponse from(final MemberSignUpResult signUpResult) {
        return new MemberSignUpResponse(signUpResult.name());
    }
}
