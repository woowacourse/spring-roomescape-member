package roomescape.entity;

import roomescape.exception.impl.InvalidLoginException;
import roomescape.exception.impl.NameContainsNumberException;
import roomescape.exception.impl.OverMaxNameLengthException;

public class Member {

    private static final int MAX_NAME_LENGTH = 10;

    private final Long id;
    private final String name;
    private final String email;
    private final String password;
    private final Role role;

    private Member(
            final Long id,
            final String name,
            final String email,
            final String password,
            final Role role
    ) {
        validateMaxNameLength(name);
        validateNameDoesNotContainsNumber(name);
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public static Member beforeMemberSave(String name, String email, String password) {
        return new Member(null, name, email, password, Role.USER);
    }

    public static Member beforeAdminSave(String name, String email, String password) {
        return new Member(null, name, email, password, Role.ADMIN);
    }

    public static Member afterSave(
            long id,
            String name,
            String email,
            String password,
            Role role
    ) {
        return new Member(id, name, email, password, role);
    }

    private void validateMaxNameLength(final String name) {
        if (name.length() > MAX_NAME_LENGTH) {
            throw new OverMaxNameLengthException();
        }
    }

    private void validateNameDoesNotContainsNumber(final String name) {
        for (char c : name.toCharArray()) {
            if (Character.isDigit(c)) {
                throw new NameContainsNumberException();
            }
        }
    }

    public void validatePassword(final String password) {
        if (!this.password.equals(password)) {
            throw new InvalidLoginException();
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
