package roomescape.domain;

public class Member {
    private final Long id;
    private final Name name;
    private final Role role;
    private final String email;
    private final String password;

    public Member(String email, String password) {
        this(null, null, null, email, password);
    }

    public Member(Long id, Name name, Role role, String email) {
        this(id, name, role, email, null);
    }

    public Member(Long id, Name name, Role role, String email, String password) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.email = email;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public Name getName() {
        return name;
    }

    public String getNameValue() {
        return name.getValue();
    }

    public String getRoleName() {
        return role.name();
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
