package roomescape.member.domain;

import roomescape.handler.exception.CustomBadRequest;
import roomescape.handler.exception.CustomException;
import java.util.Objects;
import java.util.regex.Pattern;

public class Member {

    private final Pattern emailPattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private final Long id;
    private final String name;
    private final String email;
    private final String password;
    private final MemberRole role;

    public Member(Long id, String name, String email, String password, MemberRole role) {
        validateName(name);
        validateEmail(email);

        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    private void validateName(String name) {
        if (name.isEmpty() || name.length() > 10) {
            throw new CustomException(CustomBadRequest.INVALID_NAME_LENGTH);
        }
    }

    private void validateEmail(String email) {
        if (!emailPattern.matcher(email).matches()) {
            throw new CustomException(CustomBadRequest.INVALID_EMAIL);
        }
    }

    public boolean isAdmin() {
        return role.equals(MemberRole.ADMIN);
    }

    public boolean hasCredential(String credential) {
        return password.equals(credential);
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

    public MemberRole getRole() {
        return role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return Objects.equals(id, member.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
