package roomescape.dto;

import roomescape.domain.member.Member;

public record MemberRequest(
        String name,
        String email,
        String password
) {

    public Member toEntity() {
        return new Member(null, name, email, password);
    }
}
