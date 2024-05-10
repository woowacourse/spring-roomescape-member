package roomescape.domain;

public class Member {
    private final Long id;
    private final MemberName name;
    private final EmailAddress email;
    private final Password password;

    public Member(Long id, MemberName name, EmailAddress email, Password password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public Member(long id, String name, String email, String password) {
        this(id, new MemberName(name), new EmailAddress(email), new Password(password));
    }

    public Member(String name, String email, String password) {
        this(null, new MemberName(name), new EmailAddress(email), new Password(password));
    }

    public Member(long id, Member member) {
        this(id, member.name, member.email, member.password);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getName();
    }

    public String getEmail() {
        return email.getAddress();
    }

    public boolean isPasswordMatches(String value) {
        return password.matches(value);
    }

    public String getPassword() {
        return password.getPassword();
    }
}
