package roomescape.dto.response;

import roomescape.domain.Member;

public record MemberLoginResponse(String name) {
    public static MemberLoginResponse from(Member member) {
        return new MemberLoginResponse(
                member.getName()
        );
    }
}
