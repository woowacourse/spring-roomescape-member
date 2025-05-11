package roomescape.member.domain;

import java.util.regex.Pattern;
import roomescape.exception.custom.AuthorizationException;
import roomescape.exception.custom.InvalidInputException;

public class Member {

    private static final int MAX_LENGTH = 255;
    private static final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    private final long id;
    private final Role role;
    private final String name;
    private final String email;
    private final String password;

    public Member(final long id,
                  final Role role,
                  final String name,
                  final String email,
                  final String password) {
        validateInvalidInput(role, name, email, password);

        this.id = id;
        this.role = role;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public static Member of(long id, String role, String name, String email, String password) {
        return new Member(id, Role.valueOf(role), name, email, password);
    }

    public void validateRightPassword(final String inputPassword) {
        if (!password.equals(inputPassword)) {
            throw new AuthorizationException("비밀 번호가 옳지 않습니다");
        }
    }

    private void validateInvalidInput(final Role role, final String name,
                                      final String email, final String password) {
        validateNotNull(role, name, email, password);
        validateValidLength(name, email, password);
        validateEmail(email);
    }

    private void validateNotNull(final Role role, final String name,
                                 final String email, final String password) {
        if (role == null) {
            throw new InvalidInputException("멤버 역할은 빈 값이 입력될 수 없습니다");
        }
        if (name == null || name.isBlank()) {
            throw new InvalidInputException("멤버 명은 빈 값이 입력될 수 없습니다");
        }
        if (email == null || email.isBlank()) {
            throw new InvalidInputException("이메일은 빈 값이 입력될 수 없습니다");
        }
        if (password == null || password.isBlank()) {
            throw new InvalidInputException("비밀 번호는 빈 값이 입력될 수 없습니다");
        }
    }

    private void validateValidLength(final String name, final String email, final String password) {
        if (name.length() > MAX_LENGTH) {
            throw new InvalidInputException("멤버 명은 255자를 초과할 수 없습니다");
        }
        if (email.length() > MAX_LENGTH) {
            throw new InvalidInputException("이메일은 255자를 초과할 수 없습니다");
        }
        if (password.length() > MAX_LENGTH) {
            throw new InvalidInputException("비밀 번호는 255자를 초과할 수 없습니다");
        }
    }

    private void validateEmail(final String email) {
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new InvalidInputException("올바르지 않은 이메일 형식입니다: " + email);
        }
    }

    public long getId() {
        return id;
    }

    public Role getRole() {
        return role;
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
