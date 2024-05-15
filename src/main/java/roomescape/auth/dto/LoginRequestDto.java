package roomescape.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDto(
        @NotBlank(message = "비밀번호를 입력해야 합니다.") String password,
        @Email(message = "이메일 형식으로 입력해야 합니다.(ex: email@email.com)") String email
) {
}
