package roomescape.user.dto;

import jakarta.validation.constraints.NotBlank;

public record UserRequest(
        @NotBlank(message = "유저 이름은 공백일 수 없습니다.")
        String name
) {
}
