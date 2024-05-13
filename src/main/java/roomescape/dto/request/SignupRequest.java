package roomescape.dto.request;

public record SignupRequest(String email, String password, String name) {

    public SignupRequest {
        validate(email, password, name);
    }

    private void validate(String email, String password, String name) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("이메일은 빈 값일 수 없습니다. 다시 입력해주세요.");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("비밀번호는 빈 값일 수 없습니다. 다시 입력해주세요.");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("이름은 빈 값일 수 없습니다. 다시 입력해주세요.");
        }
    }
}
