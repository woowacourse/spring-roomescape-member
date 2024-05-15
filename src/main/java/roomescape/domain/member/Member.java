package roomescape.domain.member;

import roomescape.exception.AuthorizationException;

import java.util.Objects;

public class Member {

    private final Long id;
    private final Name name;
    private final String email;
    private final String password;
    private final Role role;

    public Member(final Long id, final Name name, final String email) {
        this(id, name, email, null, null);
    }

    public Member(final Name name, final String email, final String password, final Role role) {
        this(null, name, email, password, role);
    }

    public Member(final Long id, final Name name, final String email, final String password, final Role role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public void checkIncorrectPassword(final String otherPassword) {
        if (!Objects.equals(this.getPassword(), otherPassword)) {
            throw new AuthorizationException("잘못된 비밀번호입니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getNameString() {
        return name.getName();
    }

    public Name getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return Objects.equals(id, member.id) && Objects.equals(name, member.name) && Objects.equals(email, member.email) && Objects.equals(password, member.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, password);
    }
}
