package roomescape.presentation.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record SignUpRequestDto(
        @NotEmpty @Email String email,
        @NotEmpty String password,
        @NotEmpty String name
) {
}
