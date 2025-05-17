package roomescape.member.controller.dto;

import jakarta.validation.constraints.NotBlank;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;

public record SignupRequest(
        @NotBlank String email,
        @NotBlank String password,
        @NotBlank String name) {

    public Member toMemberWithoutId() {
        return new Member(null, email, password, name, Role.USER);
    }

}
