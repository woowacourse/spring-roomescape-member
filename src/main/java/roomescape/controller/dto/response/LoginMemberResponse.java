package roomescape.controller.dto.response;

import roomescape.domain.member.LoginMember;

public record LoginMemberResponse(long id, String role, String name, String email, String password) {

    public static LoginMemberResponse from(final LoginMember member) {
        return new LoginMemberResponse(
                member.getId(), member.getRole(), member.getName(), member.getEmail(), member.getPassword());
    }
}
