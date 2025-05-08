package roomescape.dto;

import roomescape.entity.Member;

public record MemberRequest(String email, String password, String name) {

    public Member toMember() {
        return new Member(name, email, password);
    }
}
