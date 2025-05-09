package roomescape.dto.response;

import roomescape.domain.Member;

public record MemberLoginCheckResponse(String name) {
    public static MemberLoginCheckResponse from(Member member) {
        return new MemberLoginCheckResponse(member.getName());
    }
}
