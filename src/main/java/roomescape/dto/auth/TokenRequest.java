package roomescape.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record TokenRequest(
        @NotBlank(message = "비밀번호를 입력해 주세요.")
        @Size(min = 4, max = 20, message = "비밀번호는 최소 4글자, 최대 20글자로 작성해주세요.")
        String password,
        @NotBlank(message = "이메일을 입력해 주세요.")
        @Pattern(regexp = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$", message = "이메일 형식에 맞게 입력해 주세요.")
        String email) {

}
