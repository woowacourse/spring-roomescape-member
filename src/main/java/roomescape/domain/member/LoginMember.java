package roomescape.domain.member;

import roomescape.exception.custom.AuthorizationException;
import roomescape.exception.custom.InvalidInputException;

public class LoginMember {

    private static final int MAX_LENGTH = 255;

    private final long id;
    private final String role;
    private final String name;
    private final String email;
    private final String password;

    public LoginMember(final long id,
                       final String role,
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

    public void validateRightPassword(final String inputPassword) {
        if (!password.equals(inputPassword)) {
            throw new AuthorizationException("비밀 번호가 옳지 않습니다");
        }
    }

    private void validateInvalidInput(final String role, final String name,
                                      final String email, final String password) {
        validateNotNull(role, name, email, password);
        validateValidLength(role, name, email, password);
    }

    private void validateNotNull(final String role, final String name,
                                 final String email, final String password) {
        if (role == null || role.isBlank()) {
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

    private void validateValidLength(final String role, final String name,
                                     final String email, final String password) {
        if (role.length() > MAX_LENGTH) {
            throw new InvalidInputException("멤버 역할은 255자를 초과할 수 없습니다");
        }
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

    public long getId() {
        return id;
    }

    public String getRole() {
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
