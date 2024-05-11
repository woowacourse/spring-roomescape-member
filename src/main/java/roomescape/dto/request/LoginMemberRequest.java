package roomescape.dto.request;

import roomescape.domain.member.LoginMember;

public record LoginMemberRequest(Long id, String name, String email) {

    public LoginMember toLoginMember() {
        return new LoginMember(id, name, email);
    }
}
