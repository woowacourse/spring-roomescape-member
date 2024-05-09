package roomescape.domain;

public class Member {

    private final Long id;
    private final Name name;
    private final Email email;
    private final Role role;
    private final String password;

    public Member(Long id, Name name, Email email, Role role, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.password = password;
    }

    public Member(Name name, Email email, Role role, String password) {
        this(null, name, email, role, password);
    }

    public boolean isMatchPassword(String password) {
        return this.password.equals(password);
    }

    public Long getId() {
        return id;
    }

    public Name getName() {
        return name;
    }

    public Email getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }

    public String getPassword() {
        return password;
    }
}
