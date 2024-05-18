package roomescape.dto.response;

import roomescape.domain.LoginMember;

public record MemberLoginResponse(String name) {
    public static MemberLoginResponse from(LoginMember member) {
        return new MemberLoginResponse(member.name());
    }
}
