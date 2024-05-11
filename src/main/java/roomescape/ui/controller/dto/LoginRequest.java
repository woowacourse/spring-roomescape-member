package roomescape.ui.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import roomescape.application.dto.request.TokenCreationRequest;

public record LoginRequest(
        @Email(message = "올바른 이메일 형식이 아닙니다.")
        String email,

        @NotBlank(message = "비밀번호는 필수입니다.")
        String password
) {
    public TokenCreationRequest toTokenCreationRequest() {
        return new TokenCreationRequest(email, password);
    }
}
