package roomescape.domain.member;

import java.util.Objects;

public class Member {
    private final Long id;
    private final Role role;
    private final MemberName name;
    private final Email email;
    private final Password password;

    public Member(Long id, Role role, MemberName name, Email email, Password password) {
        this.id = id;
        this.role = role;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public Member(Role role, MemberName name, Email email, Password password) {
        this(null, role, name, email, password);
    }

    public Member(Long id, Member member) {
        this(id, member.role, member.name, member.email, member.password);
    }

    public Long getId() {
        return id;
    }

    public Role getRole() {
        return role;
    }

    public MemberName getName() {
        return name;
    }

    public Email getEmail() {
        return email;
    }

    public Password getPassword() {
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
