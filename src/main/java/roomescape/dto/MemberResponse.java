package roomescape.dto;

import roomescape.entity.Member;

public record MemberResponse(
        long id,
        String name,
        String email,
        String password
) {

    public static MemberResponse from(final Member member) {
        return new MemberResponse(member.getId(), member.getName(), member.getEmail(), member.getPassword());
    }
}
