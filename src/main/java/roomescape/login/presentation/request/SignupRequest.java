package roomescape.login.presentation.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import roomescape.member.business.domain.Member;

public record SignupRequest(
        @Email String email,
        @NotNull String password,
        @NotNull String name
) {

    public Member toMember() {
        return new Member(name, email, password);
    }
}
