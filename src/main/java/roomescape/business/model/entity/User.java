package roomescape.business.model.entity;

import roomescape.business.model.vo.Email;
import roomescape.business.model.vo.Id;
import roomescape.business.model.vo.Password;
import roomescape.business.model.vo.UserRole;
import roomescape.exception.business.InvalidCreateArgumentException;

public class User {

    private static final int MAX_NAME_LENGTH = 10;

    private final Id id;
    private final UserRole userRole;
    private final String name;
    private final Email email;
    private final Password password;

    private User(final Id id, final UserRole userRole, final String name, final Email email, final Password password) {
        validateMaxNameLength(name);
        validateNameDoesNotContainsNumber(name);
        this.id = id;
        this.userRole = userRole;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    private void validateMaxNameLength(final String name) {
        if (name.length() > MAX_NAME_LENGTH) {
            throw new InvalidCreateArgumentException("이름은 10자를 넘길 수 없습니다.");
        }
    }

    private void validateNameDoesNotContainsNumber(final String name) {
        for (char c : name.toCharArray()) {
            if (Character.isDigit(c)) {
                throw new InvalidCreateArgumentException("이름에 숫자는 포함될 수 없습니다.");
            }
        }
    }

    public static User create(final String name, final String email, final String password) {
        return new User(Id.issue(), UserRole.USER, name, new Email(email), Password.encode(password));
    }

    public static User restore(final String id, final String userRole, final String name, final String email, final String password) {
        return new User(Id.create(id), UserRole.valueOf(userRole), name, new Email(email), Password.plain(password));
    }

    public boolean isPasswordCorrect(final String password) {
        return this.password.matches(password);
    }

    public String id() {
        return id.value();
    }

    public String name() {
        return name;
    }

    public String email() {
        return email.value();
    }

    public String password() {
        return password.value();
    }

    public String role() {
        return userRole.name();
    }
}
