package roomescape.dto.response;

import roomescape.entity.Member;

public record MemberNameResponse(
        String name
) {
    public MemberNameResponse(Member member) {
        this(member.getName());
    }
}
