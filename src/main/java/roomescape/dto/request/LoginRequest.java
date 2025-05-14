package roomescape.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;
import roomescape.domain.regex.MemberFormat;

public record LoginRequest(
        @NotBlank(message = "[ERROR] 이메일은 빈 값이나 공백값을 허용하지 않습니다.")
        @Length(max = 255, message = "[ERROR] 255자를 초과한 값은 허용하지 않습니다.")
        @Pattern(regexp = MemberFormat.EMAIL, message = "이메일 형식을 제대로 입력해주세요.")
        String email,

        @NotBlank(message = "[ERROR] 비밀번호는 빈 값이나 공백값을 허용하지 않습니다.")
        @Length(max = 255, message = "[ERROR] 255자를 초과한 값은 허용하지 않습니다.")
        @Pattern(regexp = MemberFormat.PASSWORD, message = "영문자, 숫자, 특수기호를 포함한 8자리이상의 비밀번호를 입력해주세요.")
        String password
) {

}
