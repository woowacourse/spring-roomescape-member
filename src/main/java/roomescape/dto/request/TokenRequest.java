package roomescape.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record TokenRequest(

        @NotBlank(message = "이메일은 필수이며 공백일 수 없습니다.")
        @Email
        String email,

        @NotBlank(message = "비밀번호는 필수이며 공백일 수 없습니다.")
        String password
) {
}
