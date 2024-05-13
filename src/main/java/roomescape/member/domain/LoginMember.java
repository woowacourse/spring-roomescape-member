package roomescape.member.domain;

import java.util.Objects;

public class LoginMember {

    private final Long id;
    private final Name name;
    private final Email email;

    public LoginMember(Long id, Name name, Email email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public LoginMember(Name name, Email email) {
        this(null, name, email);
    }

    public LoginMember(Long id, String name, String email) {
        this(id, new Name(name), new Email(email));
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

    public Name getName() {
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
