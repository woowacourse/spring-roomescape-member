package roomescape.domain;

import java.util.Objects;

import roomescape.domain.util.Validator;

public record Member(Long id, String name, String email, Role role) {
    public static final int MAX_STRING_LENGTH = 255;

    public Member(String name, String email, Role role) {
        this(null, name, email, role);
    }

    public Member {
        Validator.notEmpty(name);
        Validator.overSize(MAX_STRING_LENGTH, name);
    }

    public Member createWithId(Long id) {
        return new Member(id, name, email, role);
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
