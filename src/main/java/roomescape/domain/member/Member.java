package roomescape.domain.member;

public class Member {
    private MemberName name;
    private String email;
    private String password;

    public Member(final MemberName name, final String email, final String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public static Member of(final String name, final String email, final String password) {
        return new Member(new MemberName(name), email, password);
    }

    public MemberName getName() {
        return name;
    }

    public String getNameValue() {
        return name.getValue();
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
