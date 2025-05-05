package roomescape.business.model.entity;

import roomescape.business.model.vo.Id;
import roomescape.exception.impl.NameContainsNumberException;
import roomescape.exception.impl.UserNameLengthExceedException;

public class User {

    private static final int MAX_NAME_LENGTH = 10;

    private final Id id;
    private final String name;
    private final String email;
    private final String password;

    private User(final Id id, final String name, final String email, final String password) {
        validateMaxNameLength(name);
        validateNameDoesNotContainsNumber(name);
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    private void validateNameDoesNotContainsNumber(final String name) {
        for (char c : name.toCharArray()) {
            if (Character.isDigit(c)) {
                throw new NameContainsNumberException();
            }
        }
    }

    private void validateMaxNameLength(final String name) {
        if (name.length() > MAX_NAME_LENGTH) {
            throw new UserNameLengthExceedException();
        }
    }

    public static User beforeSave(final String name, final String email, final String password) {
        return new User(Id.nullId(), name, email, password);
    }

    public static User afterSave(final long id, final String name, final String email, final String password) {
        return new User(Id.create(id), name, email, password);
    }

    public boolean isPasswordCorrect(final String password) {
        return this.password.equals(password);
    }

    public Long id() {
        return id.longValue();
    }

    public String name() {
        return name;
    }

    public String email() {
        return email;
    }

    public String password() {
        return password;
    }
}
