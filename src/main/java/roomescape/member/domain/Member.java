package roomescape.member.domain;

import java.util.regex.Pattern;
import roomescape.exception.custom.AuthenticationException;
import roomescape.exception.custom.InvalidInputException;

public class Member {

    private static final int NON_SAVED_STATUS = 0;
    private static final int NAME_MAX_LENGTH = 15;
    private static final int EMAIL_MAX_LENGTH = 30;
    private static final int PASSWORD_MAX_LENGTH = 20;
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

    public Member(final long id, final Member savedMember) {
        this(id, savedMember.getRole(), savedMember.getName(), savedMember.getEmail(), savedMember.getPassword());
    }

    public static Member of(long id, String role, String name, String email, String password) {
        return new Member(id, Role.valueOf(role), name, email, password);
    }

    public static Member registerUser(final String name, final String email, final String password) {
        return new Member(NON_SAVED_STATUS, Role.USER, name, email, password);
    }

    public void validateRightPassword(final String inputPassword) {
        if (!password.equals(inputPassword)) {
            throw new AuthenticationException("비밀 번호가 옳지 않습니다");
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
        if (name.length() > NAME_MAX_LENGTH) {
            throw new InvalidInputException("멤버 명은 15자를 초과할 수 없습니다");
        }
        if (email.length() > EMAIL_MAX_LENGTH) {
            throw new InvalidInputException("이메일은 30자를 초과할 수 없습니다");
        }
        if (password.length() > PASSWORD_MAX_LENGTH) {
            throw new InvalidInputException("비밀 번호는 20자를 초과할 수 없습니다");
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
