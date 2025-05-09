package roomescape.auth.dto.response;

import roomescape.auth.domain.LoginMember;

public record MemberResponse(String name) {

    public static MemberResponse from(final LoginMember member) {
        return new MemberResponse(member.getName());
    }
}
