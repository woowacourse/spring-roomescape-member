package roomescape.business.domain;

public class User {

    private Long id;

    private final String name;
    private final String email;
    private final String password;

    public User(
            final String name,
            final String email,
            final String password
    ) {
        this(null, name, email, password);
    }

    private User(
            final Long id,
            final String name,
            final String email,
            final String password
    ) {
        validateNonNull(name, email, password);
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
            final String password
    ) {
        if (id == null) {
            throw new IllegalArgumentException("id가 null 입니다.");
        }

        return new User(id, name, email, password);
    }

    private void validateNonNull(
            final String name,
            final String email,
            final String password
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
}
