package roomescape.core.domain;

import roomescape.web.exception.BadRequestException;

public class Member {
    private final Long id;
    private final String name;
    private final String email;
    private final String password;
    private final Role role;

    public Member(
            final String name,
            final String email,
            final String password,
            final Role role
    ) {
        this(null, name, email, password, role);
    }

    public Member(
            final Long id,
            final String name,
            final String email,
            final String password,
            final String role
    ) {
        this(id, name, email, password, Role.valueOf(role));
    }

    public Member(
            final Long id,
            final String name,
            final String email,
            final String password,
            final Role role
    ) {
        validateEmpty(name, email, password, role);
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    private void validateEmpty(
            final String name,
            final String email,
            final String password,
            final Role role
    ) {
        if (name == null || name.isBlank()) {
            throw new BadRequestException("사용자 이름은 null이나 빈 값일 수 없습니다.");
        }
        if (email == null || email.isBlank()) {
            throw new BadRequestException("사용자 이메일은 null이나 빈 값일 수 없습니다.");
        }
        if (password == null || password.isBlank()) {
            throw new BadRequestException("사용자 비밀번호는 null이나 빈 값일 수 없습니다.");
        }
        if (role == null) {
            throw new BadRequestException("사용자 역할은 null일 수 없습니다.");
        }
    }

    public boolean hasMatchedPassword(final String password) {
        return this.password.equals(password);
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

    public String getRoleName() {
        return role.name();
    }
}
