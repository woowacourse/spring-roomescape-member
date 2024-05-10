package roomescape.dto;

import roomescape.domain.Member;

public record MemberRequest(String name, String email, String password) {

    public Member toEntity() {
        return new Member(name, email, password);
    }
}
