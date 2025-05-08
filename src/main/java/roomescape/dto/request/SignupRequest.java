package roomescape.dto.request;

import roomescape.domain.Member;
import roomescape.domain.Role;

public record SignupRequest(
        String name,
        String email,
        String password
) {

    public Member toMember() {
        return new Member( name, email, password, Role.USER);
    }
}
