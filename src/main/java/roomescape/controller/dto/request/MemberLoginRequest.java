package roomescape.controller.dto.request;

import jakarta.validation.constraints.NotBlank;

public record MemberLoginRequest(
        @NotBlank(message = "비어있는 값입니다")
        String email,

        @NotBlank(message = "비어있는 값입니다")
        String password) {
}
