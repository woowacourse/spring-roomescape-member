package roomescape.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AuthRequest(@NotBlank String email, @NotBlank String password) {

}
