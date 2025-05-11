package roomescape.domain;

import java.util.Objects;
import roomescape.exception.InvalidAuthException;

public class Member {

    private Long id;
    private final String name;
    private final String email;
    private final String password;
    private final MemberRole role;

    private Member(final Long id, final String name, final String email, final String password, final MemberRole role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public static Member fromWithoutId(final String name, final String email, final String password) {
        if (isAdmin(name)) {
            return new Member(null, name, email, password, MemberRole.ADMIN);
        }
        return new Member(null, name, email, password, MemberRole.USER);
    }

    private static boolean isAdmin(final String name) {
        return name.equals("어드민") || name.equals("admin");
    }

    public static Member from(final Long id, final String name, final String email, final String password,
                              final MemberRole role) {
        return new Member(id, name, email, password, role);
    }

    public void validatePassword(final String password) {
        if (!this.password.equals(password)) {
            throw new InvalidAuthException("비밀번호가 일치하지 않습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public MemberRole getRole() {
        return role;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Member member = (Member) o;
        return Objects.equals(getId(), member.getId()) && Objects.equals(getName(), member.getName())
                && Objects.equals(getEmail(), member.getEmail()) && Objects.equals(getPassword(),
                member.getPassword()) && Objects.equals(getRole(), member.getRole());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getEmail(), getPassword(), getRole());
    }
}
