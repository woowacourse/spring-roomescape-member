package roomescape.controller.dto.request;

import roomescape.domain.member.Member;

public record MemberResponse(
        long id,
        String name
) {
    public static MemberResponse from(final Member member) {
        return new MemberResponse(member.getId(), member.getNameValue());
    }
}
