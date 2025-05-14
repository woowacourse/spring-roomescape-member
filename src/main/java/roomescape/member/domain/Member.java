package roomescape.member.domain;

import roomescape.member.exception.MemberFieldRequiredException;

public class Member {
    private final Long id;
    private final String name;
    private final String email;
    private final String password;
    private final Role role;

    private Member(Long id, String name, String email, String password, Role role) {
        validate(name, email, password, role);
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public static Member createWithoutId(String name, String email, String password) {
        return new Member(null, name, email, password, Role.USER);
    }

    public static Member createWithId(Long id, String name, String email, String password, Role role) {
        return new Member(id, name, email, password, role);
    }

    private void validate(String name, String email, String password, Role role) {
        validateName(name);
        validateEmail(email);
        validatePassword(password);
        validateRole(role);
    }

    private void validateRole(Role role) {
        if (role == null) {
            throw new MemberFieldRequiredException();
        }
    }

    private void validateName(String name) {
        if (name.isBlank()) {
            throw new MemberFieldRequiredException();
        }
    }

    private void validateEmail(String email) {
        if (email.isBlank()) {
            throw new MemberFieldRequiredException();
        }
    }

    private void validatePassword(String password) {
        if (password.isBlank()) {
            throw new MemberFieldRequiredException();
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
