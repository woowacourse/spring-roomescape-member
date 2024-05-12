package roomescape.service.dto.input;

import roomescape.domain.Member;

public record MemberLoginInput(String email, String password) {

    public Member toMember() {
        return Member.of(email, password);
    }
}
