package roomescape.admin.dto;

import roomescape.member.domain.Member;

public record MemberResponse(
    long id,
    String name
) {

    public static MemberResponse fromMember(Member member) {
        return new MemberResponse(
            member.getId(),
            member.getName()
        );
    }
}
