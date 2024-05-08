package roomescape.service.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record MemberLoginRequest(@Email String email, @NotBlank String password) {
}
