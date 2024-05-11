package roomescape.dto.response;

import roomescape.domain.member.LoginMember;

public record MemberNameResponse(String name) {

    public MemberNameResponse(LoginMember loginMember) {
        this(loginMember.getName().name());
    }
}
