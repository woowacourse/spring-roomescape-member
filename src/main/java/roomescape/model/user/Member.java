package roomescape.model.user;

public class Member {
    private final Long id;
    private final Name name;
    private final Email email;
    private final Password password;
    private final Role role;

    public Member(Long id, Name name, Email email, Password password, Role role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getValue();
    }

    public String getEmail() {
        return email.getValue();
    }

    public String getPassword() {
        return password.getValue();
    }

    public Role getRole() {
        return role;
    }

}
