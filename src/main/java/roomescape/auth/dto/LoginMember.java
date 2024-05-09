package roomescape.auth.dto;

import roomescape.member.domain.Member;
import roomescape.member.domain.Role;

public record LoginMember(Long id, String email, String name, String password, Role role) {

    public Member toModel() {
        return new Member(id, name, email, password, role);
    }
}
