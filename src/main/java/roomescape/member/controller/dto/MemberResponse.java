package roomescape.member.controller.dto;

import roomescape.member.domain.Member;

public record MemberResponse(long id, String role, String name, String email, String password) {

    public static MemberResponse from(final Member member) {
        return new MemberResponse(
                member.getId(), member.getRole().toString(), member.getName(), member.getEmail(), member.getPassword());
    }
}
