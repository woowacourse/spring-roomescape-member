package roomescape.auth.entity;

public class Member {
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
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public static Member beforeSave(String name, String email, String password) {
        return new Member(null, name, email, password, null);
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
