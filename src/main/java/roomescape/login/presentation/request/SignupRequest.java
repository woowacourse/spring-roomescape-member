package roomescape.login.presentation.request;

import roomescape.member.business.domain.Member;

public record SignupRequest(
        String email,
        String password,
        String name
) {

    public Member toMember() {
        return new Member(name, email, password);
    }
}
