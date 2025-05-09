package roomescape.auth.service.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import roomescape.auth.entity.Member;
import roomescape.auth.entity.Role;

public record UserSignupRequest(
        @Email
        String email,
        @NotEmpty
        String password,
        @NotEmpty
        String name
) {
        public Member toEntity() {
                return new Member(0L, name, Role.USER.name(), email, password);
        }
}
