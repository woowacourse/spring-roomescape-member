package roomescape.dto;

import roomescape.domain.member.Member;

public record MemberResponse(Long id, String name, String email) {
    public static MemberResponse from(Member member) {
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }
}
