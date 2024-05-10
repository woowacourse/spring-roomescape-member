package roomescape.dto.request;

import roomescape.domain.Member;
import roomescape.domain.Role;

public record MemberRequest(String email, String password, String name, Role role) {

    public Member toMember() {
        return new Member(email, password, name, role);
    }
}
