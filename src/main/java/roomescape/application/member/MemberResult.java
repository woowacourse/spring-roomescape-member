package roomescape.application.member;

import roomescape.domain.member.Member;

public record MemberResult(
        Long id,
        String name
) {
    public static MemberResult from(Member member) {
        return new MemberResult(member.getId(), member.getName());
    }
}
