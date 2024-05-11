package roomescape.dto;

import roomescape.Role;
import roomescape.domain.Member;

public record MemberRequest(String name, String email, String password) {

    public MemberRequest {
        InputValidator.validateNotNull(name, email, password);
        InputValidator.validateNotEmpty(name, email, password);
    }

    public Member toMember() {
        return new Member(name, email, password, Role.USER);
    }
}
