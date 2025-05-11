package roomescape.auth.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record TokenRequest(@NotBlank String email, @NotBlank String password) {
    public TokenRequest(final String email, final String password) {
        this.email = email;
        this.password = password;
    }
}
