package roomescape.business.model.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.ToString;
import roomescape.business.model.vo.Email;
import roomescape.business.model.vo.Id;
import roomescape.business.model.vo.Password;
import roomescape.business.model.vo.UserName;
import roomescape.business.model.vo.UserRole;

import java.util.Objects;

@ToString(exclude = "password")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class User {

    private final Id id;
    private final UserRole userRole;
    private final UserName name;
    private final Email email;
    private final Password password;

    public static User create(final String name, final String email, final String password) {
        return new User(Id.issue(), UserRole.USER, new UserName(name), new Email(email), Password.encode(password));
    }

    public static User restore(final String id, final String userRole, final String name, final String email, final String password) {
        return new User(Id.create(id), UserRole.valueOf(userRole), new UserName(name), new Email(email), Password.plain(password));
    }

    public boolean isPasswordCorrect(final String password) {
        return this.password.matches(password);
    }

    public String id() {
        return id.value();
    }

    public String name() {
        return name.value();
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

    @Override
    public final boolean equals(final Object o) {
        if (!(o instanceof final User user)) return false;

        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
