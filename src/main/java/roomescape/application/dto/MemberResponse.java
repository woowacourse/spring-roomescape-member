package roomescape.application.dto;

import roomescape.domain.AuthMember;
import roomescape.domain.Member;

public record MemberResponse(
        Long id,
        String name
) {

    public MemberResponse(Member member) {
        this(
                member.getId(),
                member.getName()
        );
    }

    public MemberResponse(AuthMember authMember) {
        this(
                authMember.getId(),
                authMember.getName()
        );
    }
}
