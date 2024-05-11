package roomescape.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record TokenRequest(
        @NotBlank(message = "이메일이 비어 있습니다.")
        @Email(message = "이메일 형식이 아닙니다.")
        String email,

        @NotBlank(message = "비밀번호가 비어 있습니다.")
        String password) {
}
