package roomescape.member.dto;

import roomescape.member.domain.LoginMember;

public record LoginMemberRequest(Long id, String name, String email) {

    public LoginMember toLoginMember() {
        return new LoginMember(id, name, email);
    }
}
