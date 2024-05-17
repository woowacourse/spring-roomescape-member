package roomescape.dto.member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record SignupRequest(
        @NotEmpty(message = "이메일이 입력되지 않았습니다.") @Email(message = "입력 형식이 올바르지 않습니다.") String email,
        @NotEmpty(message = "비밀번호가 입력되지 않았습니다.") String password,
        @NotEmpty(message = "이름이 입력되지 않았습니다.") String name
) {
}
