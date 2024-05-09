package roomescape.controller.dto.response;

import roomescape.domain.member.Member;

public record MemberNameResponse(
        String name
) {
    public static MemberNameResponse from(final Member member) {
        return new MemberNameResponse(member.getNameValue());
    }
}
