package roomescape.domain;

public record Member(Long id, String name, String email, String password) {
    public Member(String name, String email, String password) {
        this(null, name, email, password);
    }

    public Member createWithId(Long id) {
        return new Member(id, name, email, password);
    }
}
