package roomescape.dto;

import roomescape.entity.Member;

public record MemberResponse(long id, String name) {

    public MemberResponse(Member member) {
        this(
                member.getId(),
                member.getName()
        );
    }
}
