package roomescape.dto.request;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record LoginRequest(
        @NotBlank(message = "[ERROR] 이메일은 빈 값이나 공백값을 허용하지 않습니다.")
        @Length(max = 255, message = "[ERROR] 255자를 초과한 값은 허용하지 않습니다.")
        String email,
        @NotBlank(message = "[ERROR] 비밀번호는 빈 값이나 공백값을 허용하지 않습니다.")
        @Length(max = 255, message = "[ERROR] 255자를 초과한 값은 허용하지 않습니다.")
        String password
) {

}
