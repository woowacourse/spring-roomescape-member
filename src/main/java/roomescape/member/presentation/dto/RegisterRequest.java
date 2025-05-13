package roomescape.member.presentation.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import roomescape.member.domain.Role;

public record RegisterRequest(@NotBlank @Email String email,
                              @NotBlank String password,
                              @NotBlank String name,
                              @Nullable Role role) {
    public RegisterRequest {
        if (role == null) {
            role = Role.USER;
        }
    }
}
