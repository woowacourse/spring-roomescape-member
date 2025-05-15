package roomescape.domain;

import java.util.Objects;

public class Member {

    private static final int MAX_LENGTH = 255;
    private static final Long DEFAULT_ID = 1L;

    private Long id;
    private String name;
    private String email;
    private String password;
    private String role;

    public Member(final Long id, final String name, final String email, final String password, final String role) {
        validateField(name, email, password, role);
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public static Member createMemberWithoutId(final String name,
                                               final String email,
                                               final String password,
                                               final String role) {
        return new Member(DEFAULT_ID, name, email, password, role);
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

    public Long getId() {
        return id;
    }

    public String getRole() {
        return role;
    }

    public boolean isNotAdmin() {
        return !role.equals("ADMIN");
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Member member = (Member) o;
        return Objects.equals(id, member.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    private void validateField(String name, String email, String password, String role) {
        validateName(name);
        validatePassword(password);
        validateRole(role);
        validateEmail(email);
    }

    private void validateName(String name) {
        if (name.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("[ERROR] 허용되지 않는 이름 길이입니다.");
        }
    }

    private void validatePassword(String password) {
        if (password.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("[ERROR] 허용되지 않는 비밀번호 길이입니다.");
        }
    }

    private void validateRole(String role) {
        if (role.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("[ERROR] 허용되지 않는 역할 길이입니다.");
        }
    }

    private void validateEmail(String email) {
        if (email.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("[ERROR] 허용되지 않는 이메일 길이입니다.");
        }
    }
}
