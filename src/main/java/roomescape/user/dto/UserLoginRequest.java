package roomescape.user.dto;

import jakarta.validation.constraints.NotBlank;

public record UserLoginRequest(
        @NotBlank(message = "로그인 이름은 필수입니다.")
        String name
) {
}