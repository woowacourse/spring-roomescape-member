package roomescape.domain;

public record Member(
        Long id,
        String name,
        String email,
        String password,
        MemberRole role
) {

    private static final MemberRole DEFAULT_ROLE = MemberRole.normal;

    public Member(String name, String email, String password) {
        this(null, name, email, password, DEFAULT_ROLE);
    }

    public Member(String name, String email, String password, String role) {
        this(null, name, email, password, MemberRole.valueOf(role));
    }

    public Member(Long id, String name, String email, String password, String role) {
        this(id, name, email, password, MemberRole.valueOf(role));
    }

    public Member withId(Long id) {
        return new Member(id, name, email, password, role);
    }
}
