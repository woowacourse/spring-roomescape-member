package roomescape.controller.dto.response;

import roomescape.domain.member.Member;

public record MemberResponse(
        String name
) {
    public static MemberResponse from(final Member member) {
        return new MemberResponse(member.getNameValue());
    }
}
