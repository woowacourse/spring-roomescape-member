package roomescape.user.entity;

import roomescape.common.vo.Role;

public class User {
    private final Long id;
    private final String email;
    private final String password;
    private final String name;
    private final Role role;

    public User(Long id, String email, String password, String name, Role role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
    }

    public static User withoutId(String email, String password, String name, Role role) {
        return new User(null, email, password, name, role);
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public Role getRole() {
        return role;
    }
}
