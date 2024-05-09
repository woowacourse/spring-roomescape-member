package roomescape.dto;

import static roomescape.dto.InputValidator.validateNotBlank;
import static roomescape.dto.InputValidator.validateNotNull;

public record LogInRequest(String email, String password) {

    public LogInRequest {
        System.out.println("이메일은" + email);
        System.out.println("비밀번호는" + password);
        validateNotNull(email, password);
        validateNotBlank(email, password);
    }
}
