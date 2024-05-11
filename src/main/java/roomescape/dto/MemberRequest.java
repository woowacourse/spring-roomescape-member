package roomescape.dto;

import roomescape.domain.Member;

public record MemberRequest(String email, String password, String name) {

    public Member toEntity() {
        return new Member(name, email, password);
    }
}
