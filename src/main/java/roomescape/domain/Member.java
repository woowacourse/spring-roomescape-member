package roomescape.domain;

public class Member {

    private Long id;
    private final String name;
    private final String email;
    private final String password;

    public Member(final String name, final String email, final String password) {
        this.id = null;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
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
