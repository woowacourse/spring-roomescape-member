package roomescape.service.dto.login;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(@NotBlank(message = "비밀 번호는 빈칸일 수 없습니다.") String password,
                           @NotBlank(message = "이메일은 빈칸일 수 없습니다.") @Email(message = "올바르지 않은 이메일 형식입니다.") String email) {
}
