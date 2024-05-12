package roomescape.member.dto;

import roomescape.member.domain.LoginMember;
import roomescape.member.domain.Member;

public record LoginMemberRequest(Long id, String name, String email) {

    public LoginMemberRequest(Member member) {
        this(member.getId(), member.getName().name(), member.getEmail().email());
    }

    public LoginMember toLoginMember() {
        return new LoginMember(id, name, email);
    }
}
