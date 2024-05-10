package roomescape.dto;

import roomescape.exception.NullRequestParameterException;

public record RegisterRequestDto(String name, String email, String password) {

    public RegisterRequestDto {
        if (name == null || name.isBlank()) {
            throw new NullRequestParameterException("이름을 입력하여야 합니다.");
        }
        if (email == null || email.isBlank()) {
            throw new NullRequestParameterException("이메일을 입력하여야 합니다.");
        }
        if (password == null || password.isBlank()) {
            throw new NullRequestParameterException("비밀번호를 입력하여야 합니다.");
        }
    }
}
