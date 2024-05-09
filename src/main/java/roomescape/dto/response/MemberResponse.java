package roomescape.dto.response;

import roomescape.domain.Member;

public record MemberResponse(Long id, String email, String name) {

    public MemberResponse(Member member) {
        this(member.getId(), member.getEmail().getEmail(), member.getName().getName());
    }

    public static MemberResponse from(Member member) {
        return new MemberResponse(member.getId(), member.getEmail().getEmail(), member.getName().getName());
    }
}
