package roomescape.member.controller.dto;

import roomescape.member.domain.LoginMember;

public record LoginMemberResponse(long id, String role, String name, String email, String password) {

    public static LoginMemberResponse from(final LoginMember member) {
        return new LoginMemberResponse(
                member.getId(), member.getRole(), member.getName(), member.getEmail(), member.getPassword());
    }
}
