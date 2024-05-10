package roomescape.member.dto;

import roomescape.member.domain.Member;

public record MemberResponse(String name) {

    public static MemberResponse toResponse(Member member) {
        return new MemberResponse(member.getName());
    }
}
