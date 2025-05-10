package roomescape.dto.auth;

import jakarta.validation.constraints.NotBlank;
import roomescape.model.Member;

public record LoginRequestDto(
        @NotBlank(message = "email은 null이거나 공백일 수 없습니다")
        String email,
        @NotBlank(message = "패스워드는 null이거나 공백일 수 없습니다")
        String password
) {
    public Member convertToUser() {
        return new Member(
                email,
                password
        );
    }
}
