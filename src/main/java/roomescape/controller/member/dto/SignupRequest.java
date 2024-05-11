package roomescape.controller.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SignupRequest(
        @Email
        String email,

        @NotBlank
        String password, //TODO 커스텀 애노테이션 만들어볼까?

        @NotBlank
        String name) {
}
