package roomescape.member.controller.dto;

import roomescape.member.domain.Member;
import roomescape.member.domain.Role;

public record SignupRequest(String email, String password, String name) {

    public Member toMemberWithoutId() {
        return new Member(null, email, password, name, Role.USER);
    }

}
