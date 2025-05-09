package roomescape.domain;

public record User(
        Long id,
        String name,
        String email,
        String password,
        String role
) {

    private static final String DEFAULT_ROLE = "normal";

    public User(String name, String email, String password) {
        this(null, name, email, password, DEFAULT_ROLE);
    }

    public User(String name, String email, String password, String role) {
        this(null, name, email, password, role);
    }

    public User withId(Long id) {
        return new User(id, name, email, password, role);
    }
}
