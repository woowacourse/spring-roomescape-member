package roomescape.auth.dto;

import jakarta.validation.constraints.Email;

public record LoginRequest(@Email String email, String password) {
}
