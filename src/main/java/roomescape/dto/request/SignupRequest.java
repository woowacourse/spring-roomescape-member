package roomescape.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberName;

public record SignupRequest(
        @NotNull
        String name,

        @NotNull
        @Email
        String email,

        @NotNull
        String password
) {
    public Member toEntity() {
        return new Member(new MemberName(name), email, password);
    }
}
