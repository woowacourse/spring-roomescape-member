package roomescape.service.member.dto;

import jakarta.validation.constraints.NotBlank;
import roomescape.domain.member.Member;

public record MemberSignUpRequest(
        @NotBlank String name,
        @NotBlank String email,
        @NotBlank String password
) {
    public Member toMember() {
        return new Member(name, email, password);
    }
}
