package roomescape.application.dto;

import roomescape.domain.LoginMember;
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

    public MemberResponse(LoginMember loginMember) {
        this(
                loginMember.getId(),
                loginMember.getName()
        );
    }
}
