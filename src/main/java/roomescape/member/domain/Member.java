package roomescape.member.domain;

import java.util.Objects;

public class Member {
    private final Long id;
    private final String name;
    private final MemberRole memberRole;
    private final String email;
    private final String password;

    public Member(final Long id, final String name, final MemberRole memberRole, final String email, final String password) {
        this.id = id;
        this.name = name;
        this.memberRole = memberRole;
        this.email = email;
        this.password = password;
    }

    public static Member of(final Long id, final Member member) {
        return new Member(id, member.name, member.memberRole, member.email, member.password);
    }

    public boolean hasNotSamePassword(final String password) {
        return !this.password.equals(password);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public MemberRole getMemberRole() {
        return memberRole;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
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
        return Objects.equals(id, member.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
