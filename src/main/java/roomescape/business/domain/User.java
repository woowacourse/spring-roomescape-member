package roomescape.business.domain;

public class User {

    private Long id;

    private final String name;
    private final String email;
    private final String password;

    private final Role role;

    public User(
            final String name,
            final String email,
            final String password,
            final Role role
    ) {
        this(null, name, email, password, role);
    }

    private User(
            final Long id,
            final String name,
            final String email,
            final String password,
            final Role role
    ) {
        this.role = role;
        validateNonNull(name, email, password, role);
        validateNotBlank(name, email, password);

        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public static User createWithId(
            final Long id,
            final String name,
            final String email,
            final String password,
            final Role role
    ) {
        if (id == null) {
            throw new IllegalArgumentException("id가 null 입니다.");
        }

        return new User(id, name, email, password, role);
    }

    private void validateNonNull(
            final String name,
            final String email,
            final String password,
            final Role role
    ) {
        if (name == null) {
            throw new IllegalArgumentException("name이 null 입니다.");
        }
        if (email == null) {
            throw new IllegalArgumentException("email이 null 입니다.");
        }
        if (password == null) {
            throw new IllegalArgumentException("password가 null 입니다.");
        }
        if (role == null) {
            throw new IllegalArgumentException("role이 null 입니다.");
        }
    }

    private void validateNotBlank(
            final String name,
            final String email,
            final String password
    ) {
        if (name.isBlank()) {
            throw new IllegalArgumentException("name이 empty 입니다.");
        }
        if (email.isBlank()) {
            throw new IllegalArgumentException("email이 empty 입니다.");
        }
        if (password.isBlank()) {
            throw new IllegalArgumentException("password가 empty 입니다.");
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
