package roomescape.domain.member;

public class Member {

    private final Name name;
    private final Email email;
    private final Password password;

    public Member(Name name, Email email, Password password) {
        this.name = name;
        this.email = email;
        this.password = password;
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
}
