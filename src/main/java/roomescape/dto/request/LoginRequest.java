package roomescape.dto.request;

import jakarta.validation.constraints.NotNull;

public record LoginRequest(
        @NotNull(message = "[ERROR] 비밀번호는 빈 값을 허용하지 않습니다.")
        String password,
        @NotNull(message = "[ERROR] 이메일은 빈 값을 허용하지 않습니다.")
        String email) {
}
