package roomescape.member;

public class Member {

    private final Long id;
    private final String email;
    private final String password;
    private final String name;

    public Member(final Long id, final String email, final String password, final String name) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public Member(final String email, final String password, final String name) {
        this(null, email, password, name);
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
}
