package roomescape.controller.request;

import roomescape.domain.Member;
import roomescape.domain.Role;

public record UserSignUpRequest(String name, String email, String password) {
    public Member toEntity() {
        return new Member(name, email, password, Role.USER);
    }
}
