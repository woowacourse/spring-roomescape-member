package roomescape.domain.member;

public class Member {
    private Long id;
    private MemberName name;
    private Email email;
    private Password password;

    public Member(Long id, String name, String email, String password) {
        this.id = id;
        this.name = new MemberName(name);
        this.email = new Email(email);
        this.password = new Password(password);
    }

    public Member(Long id, MemberName name, Email email, Password password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
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
}
