package roomescape.domain.member;

import java.util.Objects;
import roomescape.common.auth.PasswordEncryptor;
import roomescape.common.exception.auth.InvalidAuthException;

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

    public static Member from(final Long id, final String name, final String email, final String password,
                              final MemberRole role) {
        final String encryptPassword = getEncryptPassword(password);
        return new Member(id, name, email, encryptPassword, role);
    }

    public static Member fromWithoutId(final String name, final String email, final String password) {
        final String hashPassword = getEncryptPassword(password);
        if (isAdmin(name)) {
            return new Member(null, name, email, hashPassword, MemberRole.ADMIN);
        }
        return new Member(null, name, email, hashPassword, MemberRole.USER);
    }

    public static Member fromWithoutPassword(final Long id, final String name, final String email,
                                             final MemberRole role) {
        return new Member(id, name, email, null, role);
    }

    private static boolean isAdmin(final String name) {
        return name.equals("어드민") || name.equals("admin");
    }

    private static String getEncryptPassword(final String password) {
        return PasswordEncryptor.encrypt(password);
    }

    public void validatePassword(final String password) {
        if (isNotMatches(password)) {
            throw new InvalidAuthException("비밀번호가 일치하지 않습니다.");
        }
    }

    private boolean isNotMatches(final String password) {
        return !PasswordEncryptor.matches(password, this.password);
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
        return Objects.equals(getId(), member.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}

