package roomescape.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import roomescape.domain.RegistrationDetails;

public record RegistrationRequestDto(
        @NotBlank(message = "[ERROR] 이름은 1글자 이상으로 이루어져야 합니다.") String name,
        @Email(message = "[ERROR] 이메일 형식으로 입력해 주세요.") String email,
        @Size(min = 8, max = 50, message = "[ERROR] 비밀번호는 8자 이상, 50자 이하여야 합니다.") String password
) {

    public RegistrationDetails createRegistrationDetails() {
        return new RegistrationDetails(name, email, password);
    }
}
