package roomescape.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MemberCreationRequest(
        @NotNull(message = "비어있는 이메일로는 회원가입이 불가능합니다.")
        @Email(message = "이메일 형식을 제대로 입력해주세요.")
        String email,
        @NotBlank(message = "비어있는 비밀번호로는 회원가입이 불가능합니다.")
        String password,
        @NotBlank(message = "비어있는 이름로는 회원가입이 불가능합니다.")
        String name
) {

}
