package roomescape.service.dto.member;

import roomescape.domain.member.Member;

public record MemberResponse(Long id, String name) {

    public static MemberResponse of(Member member) {
        return new MemberResponse(member.getId(), member.getName());
    }
}
