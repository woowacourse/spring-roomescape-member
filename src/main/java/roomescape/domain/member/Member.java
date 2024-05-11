package roomescape.domain.member;

public class Member {
    private final Long id;
    private final MemberName name;
    private final Email email;
    private final Password password;
    private final String role;

    public Member(Long id, String name, String email, String password, String role) {
        this(id, new MemberName(name), new Email(email), new Password(password), role);
    }

    public Member(Long id, MemberName name, Email email, Password password, String role) {
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
        return name.value();
    }

    public String getEmail() {
        return email.value();
    }

    public String getPassword() {
        return password.value();
    }

    public String getRole() {
        return role;
    }
}
