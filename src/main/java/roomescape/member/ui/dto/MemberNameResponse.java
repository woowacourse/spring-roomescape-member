package roomescape.member.ui.dto;

import roomescape.member.domain.Member;

public record MemberNameResponse(
        String name
) {

    public static MemberNameResponse from(final Member member) {
        return new MemberNameResponse(member.getName());
    }
}
