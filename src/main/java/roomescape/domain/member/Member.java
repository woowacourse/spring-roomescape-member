package roomescape.domain.member;

import java.util.Objects;

public class Member {
    private final Long id;
    private final MemberName name;
    private final Email email;
    private final Password password;

    public Member(Long id, MemberName name, Email email, Password password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public Member(MemberName name, Email email, Password password) {
        this(null, name, email, password);
    }

    public Member(Long id, Member member) {
        this(id, member.name, member.email, member.password);
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
