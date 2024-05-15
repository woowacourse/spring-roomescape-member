package roomescape.service.dto;

import roomescape.domain.LoginMember;

public record LoginMemberRequest(long id, String name, String email, String password, String role) {
    public LoginMember toLoginMember(LoginMemberRequest loginMemberRequest) {
        return new LoginMember(loginMemberRequest.id, loginMemberRequest.name, loginMemberRequest.email,
                loginMemberRequest.password,
                loginMemberRequest.role());
    }
}
