package roomescape.service.dto.member;

import jakarta.validation.constraints.NotBlank;
import roomescape.domain.Member;

public record MemberSignUpRequest(
        @NotBlank String name,
        @NotBlank String email,
        @NotBlank String password
) {
    public Member toMember() {
        return new Member(name, email, password);
    }
}
