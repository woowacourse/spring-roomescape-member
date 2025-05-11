package roomescape.member.controller.dto;

import roomescape.member.domain.Member;

public record SignupRequest(String email, String password, String name) {

    public Member toMemberWithoutId() {
        return new Member(null, email, password, name, "MEMBER");
    }

}
