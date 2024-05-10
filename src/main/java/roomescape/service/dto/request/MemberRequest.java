package roomescape.service.dto.request;

import roomescape.domain.Member;
import roomescape.domain.Role;

public record MemberRequest(String name, String email, String password, Role role) {

    public Member toEntity() {
        return new Member(name, email, password, role);
    }

}
