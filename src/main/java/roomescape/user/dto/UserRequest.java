package roomescape.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserRequest(
        @NotNull(message = "유저 이름은 필수입니다.")
        @NotBlank(message = "유저 이름은 공백일 수 없습니다.")
        String name
) {
}
