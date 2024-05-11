package roomescape.dto;

import roomescape.domain.Member;

public record MemberResponse(String name) {

    public static MemberResponse from(Member member) {
        return new MemberResponse(member.getName());
    }
}
