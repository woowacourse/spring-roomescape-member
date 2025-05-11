package roomescape.member.dto;

public record LoginRequest(String email, String password) {

    public LoginRequest {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("[ERROR] 이메일을 입력해주세요.");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("[ERROR] 비밀번호를 입력해주세요.");
        }
    }
}
