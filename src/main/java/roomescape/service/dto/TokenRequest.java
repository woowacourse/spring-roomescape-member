package roomescape.service.dto;

import jakarta.validation.constraints.Email;

public record TokenRequest(String password, @Email String email) {
}
