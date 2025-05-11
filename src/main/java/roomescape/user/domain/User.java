package roomescape.user.domain;

import roomescape.common.domain.Id;

public class User {
    private final Id id;
    private final String name;
    private final String email;
    private final String password;

    public User(final Id id, final String name, final String email, final String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public static User of(final Long id, final String name, final String email, final String password) {
        return new User(Id.from(id), name, email, password);
    }

    public static User withUnassignedId(final String name, final String email, final String password) {
        return new User(Id.unassigned(), name, email, password);
    }

    public Long getId() {
        return id.getValue();
    }

    public void setId(final Long value) {
        id.setValue(value);
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
