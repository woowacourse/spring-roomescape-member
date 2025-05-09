package roomescape.dto;

import roomescape.entity.member.Member;

public record MemberRequest(
        long id,
        String name,
        String email,
        String password
) {

    public static MemberRequest from(final Member member) {
        return new MemberRequest(member.getId(), member.getName(), member.getEmail(), member.getPassword());
    }
}
