package roomescape.dto.response;

import roomescape.domain.member.Member;

public record MemberResponse(String name) {
    public MemberResponse(Member member) {
        this(member.getName());
    }
}
