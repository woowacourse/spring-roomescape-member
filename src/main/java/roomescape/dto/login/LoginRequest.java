package roomescape.dto.login;

import roomescape.domain.Member;

public record LoginRequest(String password, String email) {

    public Member toMember() {
        return new Member(null, null, email, password, null);
    }
}
