package roomescape.domain;

public record Member(
        Long id,
        String name,
        String email,
        String password,
        String role
) {

    private static final String DEFAULT_ROLE = "normal";

    public Member(String name, String email, String password) {
        this(null, name, email, password, DEFAULT_ROLE);
    }

    public Member(String name, String email, String password, String role) {
        this(null, name, email, password, role);
    }

    public Member withId(Long id) {
        return new Member(id, name, email, password, role);
    }
}
