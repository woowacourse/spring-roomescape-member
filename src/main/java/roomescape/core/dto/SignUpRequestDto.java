package roomescape.core.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class SignUpRequestDto {
    @NotBlank(message = "사용자 이름은 null이나 빈 값일 수 없습니다.")
    private String name;
    @NotBlank(message = "이메일은 null이나 빈 값일 수 없습니다.")
    @Email(message = "올바르지 않은 이메일 형식입니다.")
    private String email;
    @NotBlank(message = "비밀번호는 null이나 빈 값일 수 없습니다.")
    private String password;

    public SignUpRequestDto() {
    }

    public SignUpRequestDto(final String name, final String email, final String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
