package roomescape.controller.dto.response;

import roomescape.domain.member.LoginMember;

public record MemberResponse(String name) {

    public static MemberResponse from(final LoginMember member) {
        return new MemberResponse(member.getName());
    }
}
