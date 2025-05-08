package roomescape.auth.service.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import roomescape.auth.entity.User;

public record SignupRequest(
        @Email
        String email,
        @NotEmpty
        String password,
        @NotEmpty
        String name
) {
        public User toEntity() {
                return new User(0L, name, email, password);
        }
}
