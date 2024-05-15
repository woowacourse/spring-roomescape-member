package roomescape.domain;

import java.util.Objects;

import roomescape.domain.util.Validator;

public class Member {
    public static final int MAX_STRING_LENGTH = 255;
    private final Long id;
    private final String name;
    private final String email;
    private final Role role;

    public Member(String name, String email, Role role) {
        this(null, name, email, role);
    }

    public Member(Long id, String name, String email, Role role) {
        Validator.notEmpty(name);
        Validator.overSize(MAX_STRING_LENGTH, name);

        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public Member createWithId(Long id) {
        return new Member(id, name, email, role);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        if (id == null || member.id == null)
            return Objects.equals(email, member.email);
        return Objects.equals(id, member.id);
    }

    @Override
    public int hashCode() {
        if (id == null)
            return Objects.hash(email);
        return Objects.hash(id);
    }
}
