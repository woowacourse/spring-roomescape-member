package roomescape.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record LoginRequest(
        @Email(message = "이메일은 메일 형식만 가능합니다.")
        @NotNull(message = "이메일은 공백 문자가 불가능합니다.")
        String email,

        @NotNull(message = "이메일은 공백 문자가 불가능합니다.")
        String password) {
}
