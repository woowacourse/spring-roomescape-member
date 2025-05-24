package roomescape.domain;

import java.util.regex.Pattern;

public class LoginMember {

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";
    public static final int MIN_PASSWORD_SIZE = 8;
    public static final int MAX_PASSWORD_SIZE = 50;

    private final Long id;
    private final String name;
    private final String email;
    private final String password;
    private final Role role;

    public LoginMember(Long id, String name, String email, String password, Role role) {
        validateName(name);
        validateEmail(email);
        validatePassword(password);
        validateRole(role);

        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("[ERROR] 사용자의 이름은 1글자 이상으로 이루어져야 합니다.");
        }
    }

    private void validateEmail(String email) {
        if (email == null) {
            throw new IllegalArgumentException("[ERROR] 이메일이 없습니다.");
        }
        if (!Pattern.matches(EMAIL_REGEX, email)) {
            throw new IllegalArgumentException("[ERROR] 이메일 형식이 아닙니다.");
        }
    }

    private void validatePassword(String password) {
        if (password == null) {
            throw new IllegalArgumentException("[ERROR] 비밀번호가 없습니다.");
        }

        String trimmedPassword = password.trim();
        if (trimmedPassword.length() < MIN_PASSWORD_SIZE || trimmedPassword.length() > MAX_PASSWORD_SIZE) {
            throw new IllegalArgumentException("[ERROR] 비밀번호는 8자 이상, 50자 이하여야 합니다.");
        }
    }

    private void validateRole(Role role) {
        if (role == null) {
            throw new IllegalArgumentException("[ERROR] 사용자 권한을 명시해 주세요.");
        }
    }

    public Long getId() {
        return id;
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

    public Role getRole() {
        return role;
    }
}
