package roomescape.dto.response;

import roomescape.domain.Member;
import roomescape.domain.Role;

public record LoginMember(
        Long id,
        String email,
        String name,
        Role role
) {
    public Member toMember() {
        return new Member(id, email, null, name, role);
    }

    public boolean isNotAdmin() {
        return role != Role.ADMIN;
    }
}
