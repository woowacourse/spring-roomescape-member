package roomescape.auth.dto;

import roomescape.member.domain.Member;

public record LoginMember(Long id, String email, String name, String password) {

    public Member toModel() {
        return new Member(id, name, email, password);
    }
}
