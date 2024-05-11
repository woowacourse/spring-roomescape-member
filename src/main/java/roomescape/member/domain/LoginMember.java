package roomescape.member.domain;

import java.util.Objects;

public class LoginMember {

    private final Long id;
    private final MemberName name;
    private final Email email;

    public LoginMember(Long id, MemberName name, Email email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public LoginMember(MemberName name, Email email) {
        this(null, name, email);
    }

    public LoginMember(Long id, String name, String email) {
        this(id, new MemberName(name), new Email(email));
    }

    public LoginMember(Member member) {
        this(member.getId(), member.getName(), member.getEmail());
    }

    public boolean isId(Long id) {
        return this.id == id;
    }

    public Long getId() {
        return id;
    }

    public MemberName getName() {
        return name;
    }

    public Email getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LoginMember that = (LoginMember) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
