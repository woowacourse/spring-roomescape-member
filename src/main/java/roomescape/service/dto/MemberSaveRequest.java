package roomescape.service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import roomescape.domain.Member;

public record MemberSaveRequest(
        @NotBlank
        @Email
        String email,

        @NotBlank
        @Size(max = 30)
        String password,

        @NotBlank
        @Size(max = 15)
        String name
) {
        public Member toMember() {
                return new Member(name, email, password);
        }
}
