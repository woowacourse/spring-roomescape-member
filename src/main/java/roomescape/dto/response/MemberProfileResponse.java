package roomescape.dto.response;

import roomescape.domain.Member;

public record MemberProfileResponse(
        Long id,
        String name
) {

    public MemberProfileResponse(Member member) {
        this(member.getId(), member.getName());
    }
}
