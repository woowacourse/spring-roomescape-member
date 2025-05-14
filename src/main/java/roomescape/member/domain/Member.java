package roomescape.member.domain;

import java.util.Objects;
import roomescape.exception.BadRequestException;
import roomescape.exception.ExceptionCause;

public class Member {

    private final Long id;
    private final String name;
    private final String email;
    private final String password;
    private final Role role;

    public Member(Long id, String name, String email, String password, Role role) {
        validatePassword(password);
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public static Member createWithoutId(String name, String email, String password, Role role) {
        return new Member(null, name, email, password, role);
    }

    private void validatePassword(String password) {
        if (password.length() < 8 || password.length() > 16) {
            throw new BadRequestException(ExceptionCause.MEMBER_PASSWORD_INVALID);
        }
        if (!isStrongEnoughPassword(password)) {
            throw new BadRequestException(ExceptionCause.MEMBER_PASSWORD_INVALID);
        }
    }

    private boolean isStrongEnoughPassword(String password) {
        boolean hasLower = password.chars().anyMatch(Character::isLowerCase);
        boolean hasUpper = password.chars().anyMatch(Character::isUpperCase);
        boolean hasDigit = password.chars().anyMatch(Character::isDigit);
        boolean hasSpecial = password.chars().anyMatch(c -> !Character.isLetterOrDigit(c));
        int count = 0;
        if (hasLower) {
            count++;
        }
        if (hasUpper) {
            count++;
        }
        if (hasDigit) {
            count++;
        }
        if (hasSpecial) {
            count++;
        }
        return count >= 3;
    }

    public boolean isAdmin() {
        return role == Role.ADMIN;
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

    public String getPassword() {
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
        return Objects.hashCode(id);
    }
}
