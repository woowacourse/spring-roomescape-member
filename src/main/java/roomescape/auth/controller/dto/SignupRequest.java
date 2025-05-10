package roomescape.auth.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record SignupRequest(
        @NotBlank String name,
        @NotBlank String email,
        @NotBlank String password
) {
    public SignupRequest(final String name, final String email, final String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String email() {
        return email;
    }

    @Override
    public String password() {
        return password;
    }
}
