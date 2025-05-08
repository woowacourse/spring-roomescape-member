package roomescape.dto.member;

import roomescape.domain.Member;

public record MemberResponse(String name) {

    public static MemberResponse of(Member member) {
        return new MemberResponse(member.getName());
    }
}
