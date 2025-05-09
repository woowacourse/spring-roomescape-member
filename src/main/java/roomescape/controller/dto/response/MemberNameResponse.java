package roomescape.controller.dto.response;

import roomescape.domain.member.LoginMember;

public record MemberNameResponse(String name) {

    public static MemberNameResponse from(final LoginMember member) {
        return new MemberNameResponse(member.getName());
    }
}
