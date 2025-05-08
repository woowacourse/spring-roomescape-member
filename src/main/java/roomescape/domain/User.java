package roomescape.domain;

public record User(
        Long id,
        String name,
        String email,
        String password
) {

    public User withId(Long id) {
        return new User(id, name, email, password);
    }
}
