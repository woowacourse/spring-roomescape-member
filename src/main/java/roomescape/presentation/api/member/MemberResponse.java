package roomescape.presentation.api.member;

import roomescape.application.member.MemberResult;

public record MemberResponse(Long id, String name) {

    public static MemberResponse from(MemberResult memberResult) {
        return new MemberResponse(memberResult.id(), memberResult.name());
    }
}
