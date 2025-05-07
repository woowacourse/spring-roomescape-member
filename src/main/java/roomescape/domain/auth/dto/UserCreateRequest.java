package roomescape.domain.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserCreateRequest(@Email String email, @NotBlank String password, @NotBlank String name) {
}
