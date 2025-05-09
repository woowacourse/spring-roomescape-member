package roomescape.controller.member.dto;

import roomescape.model.Member;

public record MemberResponse(
        Long id,
        String name,
        String email,
        String password,
        String role
) {

    public static MemberResponse from(final Member member) {
        return new MemberResponse(member.id(), member.name(), member.email(), member.password(),
                member.role().toString());
    }
}
