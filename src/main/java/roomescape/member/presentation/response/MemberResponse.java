package roomescape.member.presentation.response;

import roomescape.member.business.model.entity.Member;

public record MemberResponse(
        Long id,
        String name
) {

    public static MemberResponse of(Member member) {
        return new MemberResponse(member.getId(), member.getName());
    }
}
