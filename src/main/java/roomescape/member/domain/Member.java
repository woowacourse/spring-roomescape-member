package roomescape.member.domain;

import java.util.Objects;

public class Member {
    private Long id;
    private Name name;
    private String email;
    private Role role;

    public Member(Long id, String name, String email, Role role) {
        this.id = id;
        this.name = new Name(name);
        this.email = email;
        this.role = role;
    }

    public Member(Long id, String name) {
        this(id, name, null, Role.USER);
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

    public Role getRole() {
        return role;
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
