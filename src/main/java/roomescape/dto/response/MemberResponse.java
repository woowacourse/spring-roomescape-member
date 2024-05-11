package roomescape.dto.response;

import roomescape.domain.member.Member;

public record MemberResponse(Long id, String name) {

    public MemberResponse(Member member) {
        this(member.getId(), member.getName().name());
    }
}
