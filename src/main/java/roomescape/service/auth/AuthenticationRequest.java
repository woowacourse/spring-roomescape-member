package roomescape.service.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AuthenticationRequest(
        @NotBlank(message = "이메일은 필수입니다.") @Email String email,
        @NotBlank(message = "비밀번호는 필수입니다.") String password
) {
}
