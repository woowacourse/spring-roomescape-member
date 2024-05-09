package roomescape.member.domain;

import java.util.Objects;

public class Member {
    private final MemberName name;
    private final String email;
    private final String password;

    public Member(String email, String password) {
        this((MemberName) null, email, password);
    }

    public Member(String name, String email, String password) {
        this(new MemberName(name), email, password);
    }

    private Member(MemberName name, String email, String password) {
        this.name = name;
        this.email = Objects.requireNonNull(email);
        this.password = Objects.requireNonNull(password);
    }

    public String getName() {
        return name.value();
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
