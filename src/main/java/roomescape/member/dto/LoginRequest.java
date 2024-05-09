package roomescape.member.dto;

import roomescape.member.domain.Member;

public record LoginRequest(String email, String password) {
    public Member createUser() {
        return new Member(email, password);
    }
}
