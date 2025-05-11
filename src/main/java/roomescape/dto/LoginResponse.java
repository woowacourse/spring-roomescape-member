package roomescape.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record LoginResponse(@Email String email, @NotNull String password) {
}
