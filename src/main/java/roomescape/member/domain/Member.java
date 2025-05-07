package roomescape.member.domain;

import roomescape.member.exception.MemberFieldRequiredException;

public class Member {
    private final Long id;
    private final String name;
    private final String email;
    private final String password;

    private Member(Long id, String name, String email, String password) {
        validate(name, email, password);
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public static Member createWithoutId(String name, String email, String password) {
        return new Member(null, name, email, password);
    }

    public static Member createWithId(Long id, String name, String email, String password) {
        return new Member(id, name, email, password);
    }

    private void validate(String name, String email, String password) {
        validateName(name);
        validateEmail(email);
        validatePassword(password);
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
}
