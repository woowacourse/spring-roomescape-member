package roomescape.domain;

public class Member {
    private final LoginMember loginMember;
    private final String email;
    private final String password;

    public Member(String name, String email, String password) {
        this(null, name, email, password);
    }

    public Member(long id, Member member) {
        this(id, member.loginMember.getName(), member.email, member.password);
    }

    public Member(Long id, String name, String email, String password) {
        this(new LoginMember(id, name), email, password);
    }

    public Member(LoginMember loginMember, String email, String password) {
        this.loginMember = loginMember;
        this.email = email;
        this.password = password;
    }

    public LoginMember getLoginMember() {
        return loginMember;
    }

    public long getId() {
        return loginMember.getId();
    }

    public String getName() {
        return loginMember.getName();
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "Member{" +
                "loginMember=" + loginMember +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
