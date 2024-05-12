package roomescape.dto.response;

import roomescape.domain.Member;
import roomescape.domain.Role;

public record LoginMember(
        Long id,
        String email,
        String name,
        Role role
) {
    private static final String EMPTY_PASSWORD = "Empty Password";

    public Member toMember() {
        return new Member(id, email, EMPTY_PASSWORD, name, role);
    }

    public boolean isNotAdmin() {
        return role != Role.ADMIN;
    }
}
