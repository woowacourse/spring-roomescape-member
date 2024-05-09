package roomescape.dto;

import roomescape.domain.LoginMember;

public record LoginMemberResponse(long id, String name, String email) {
    public static LoginMemberResponse from(LoginMember loginMember) {
        return new LoginMemberResponse(
                loginMember.getId(),
                loginMember.getName(),
                loginMember.getEmail()
        );
    }
}
