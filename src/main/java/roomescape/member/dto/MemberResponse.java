package roomescape.member.dto;

import roomescape.member.domain.Member;

public record MemberResponse(String name) {

    public MemberResponse(Member member) {
        this(member.getName());
    }
}
