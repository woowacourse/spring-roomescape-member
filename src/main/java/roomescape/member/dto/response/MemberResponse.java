package roomescape.member.dto.response;

import roomescape.member.Member;

public record MemberResponse(String name) {
    public static MemberResponse toDto(Member member) {
        return new MemberResponse(member.getName());
    }
}
