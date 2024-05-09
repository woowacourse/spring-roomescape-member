package roomescape.controller.login;

import roomescape.domain.Member;

public record MemberNameResponse(String name) {

    public static MemberNameResponse from(Member member) {
        return new MemberNameResponse(member.getName());
    }
}
