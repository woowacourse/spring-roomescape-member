package roomescape.application.dto;

import roomescape.domain.Member;

public record MemberNameResponse(
        String name
) {

    public MemberNameResponse(Member member) {
        this(member.getName());
    }
}
