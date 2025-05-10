package roomescape.member.domain;

public class Member {

    private final Long id;
    private final String name;
    private final String email;
    private final String password;

    private Member(final Long id, final String name, final String email, final String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public static Member createWithId(final Long id, final String name, final String email, final String password) {
        return new Member(id, name, email, password);
    }

    public static Member createWithoutId(final String name, final String email, final String password) {
        return new Member(null, name, email, password);
    }

    public boolean isSamePassword(final String password) {
        return this.password.equals(password);
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
