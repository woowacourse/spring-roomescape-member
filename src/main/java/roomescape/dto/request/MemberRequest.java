package roomescape.dto.request;

import roomescape.domain.Member;

public record MemberRequest(String email, String password, String name) {

    public Member toMember() {
        return new Member(email, password, name);
    }
}
