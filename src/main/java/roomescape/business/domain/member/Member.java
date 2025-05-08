package roomescape.business.domain.member;

public final class Member {

    private final Long id;
    private final MemberName name;
    private final Email email;
    private final MemberPassword password;

    public Member(Long id, String name, String email, String password) {
        this.id = id;
        this.name = new MemberName(name);
        this.email = new Email(email);
        this.password = new MemberPassword(password);
    }

    public Member(String email, String password, String name) {
        this(null, name, email, password);
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
