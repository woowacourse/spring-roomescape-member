package roomescape.member.domain;

import java.util.Objects;

public class Member {
    private Long id;
    private Name name;
    private String email;

    public Member(Long id, String name, String email) {
        this.id = id;
        this.name = new Name(name);
        this.email = email;
    }

    public Member(String name) {
        this(null, name, null);
    }

    public Member(Long id, String name) {
        this(id, name, null);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getName();
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Member member = (Member) o;
        return Objects.equals(getId(), member.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "Member{" + "id=" + id + ", name=" + name + ", email='" + email + '\'' + '}';
    }
}
