package roomescape.controller.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoginMember(
        @NotNull
        Long id,
        @NotBlank
        String name,
        @Email
        String email,
        @NotBlank
        String role) {
}
