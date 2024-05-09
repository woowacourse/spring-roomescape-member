package roomescape.dto.response;

import roomescape.domain.Member;

public record MemberResponse(String name) {
    public static MemberResponse from(Member member) {
        return new MemberResponse(member.name());
    }
}
