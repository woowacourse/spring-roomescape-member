package roomescape.service.dto.input;

import roomescape.domain.Member;

public record MemberCreateInput(String name, String email, String password, String role) {

    public Member toMember() {
        return Member.of(null, name, email, password, role);
    }
}
