package roomescape.member.domain;

import java.util.Objects;

public class Member {

    private final long id;
    private final String email;
    private final String password;
    private final String name;

    public Member(long id, String email, String password, String name) {
        this.email = email;
        this.id = id;
        this.password = password;
        this.name = name;
    }

    public Member (String email, String password, String name) {
        this(0, email, password, name);
    }

    public String getEmail() {
        return email;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public Member toIdAssigned(long id) {
        return new Member(id, email, password, name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Member member)) {
            return false;
        }
        return id == member.id && Objects.equals(email, member.email) && Objects.equals(password,
                member.password) && Objects.equals(name, member.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, password, name);
    }

    @Override
    public String toString() {
        return "Member{" +
                "email='" + email + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

}
