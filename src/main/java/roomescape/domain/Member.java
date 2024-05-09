package roomescape.domain;

import java.util.Objects;

public class Member {

    private final Long id;
    private final Name name;
    private final String email;
    private final String password;

    public Member(Long id, Name name, String email) {
        this(id, name, email, null);
    }

    public Member(Long id, Name name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public Name getName() {
        return name;
    }

    public String getNameValue() {
        return name.name();
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Member member = (Member) o;
        return Objects.equals(id, member.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
