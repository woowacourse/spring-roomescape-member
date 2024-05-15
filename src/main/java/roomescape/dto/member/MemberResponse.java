package roomescape.dto.member;

import roomescape.domain.member.Member;

public record MemberResponse(Long id, String name) {

    public static MemberResponse from(Member member) {
        return new MemberResponse(member.getId(), member.getName().getValue());
    }
}
