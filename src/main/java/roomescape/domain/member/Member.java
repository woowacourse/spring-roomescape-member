package roomescape.domain.member;

import java.util.Objects;

public class Member {
    private final Long id;
    private final MemberName name;
    private final Email email;
    private final Password password;
    private final Role role;

    public Member(Long id, MemberName name, Email email, Password password, Role role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public Member(MemberName name, Email email, Password password, Role role) {
        this(null, name, email, password, role);
    }

    public Member(Long id, Member member) {
        this(id, member.name, member.email, member.password, member.role);
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

    public Password getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
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
