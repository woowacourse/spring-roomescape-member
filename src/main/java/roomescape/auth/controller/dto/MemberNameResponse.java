package roomescape.auth.controller.dto;

import roomescape.member.domain.LoginMember;

public record MemberNameResponse(String name) {

    public static MemberNameResponse from(final LoginMember member) {
        return new MemberNameResponse(member.getName());
    }
}
