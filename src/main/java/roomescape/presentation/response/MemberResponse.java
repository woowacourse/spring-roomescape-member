package roomescape.presentation.response;

import roomescape.application.result.MemberResult;

public record MemberResponse(Long id, String name) {

    public static MemberResponse from(MemberResult memberResult) {
        return new MemberResponse(memberResult.id(), memberResult.name());
    }
}
