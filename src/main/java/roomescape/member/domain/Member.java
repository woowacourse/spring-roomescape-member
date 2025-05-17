package roomescape.member.domain;

public class Member {
    private final Long id;
    private final String email;
    private final String password;
    private final String name;
    private final Role role;

    public Member(Long id, String email, String password, String name, Role role) {
        validate(email, password, name, role);
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
    }

    public Member(Long id, String email, String password, String name, String role) {
        validate(email, password, name, Role.from(role));
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = Role.from(role);
    }

    public Member createMemberWithId(Long id) {
        return new Member(id, this.email, this.password, this.name, this.role);
    }

    private void validate(String email, String password, String name, Role role) {
        validateEmail(email);
        validatePassword(password);
        validateName(name);
        validateRole(role);
    }

    private void validateEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("이메일은 비어 있을 수 없습니다.");
        }
    }

    private void validatePassword(String password) {
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("비밀번호는 비어 있을 수 없습니다.");
        }
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("이름은 비어 있을 수 없습니다.");
        }
    }

    private void validateRole(Object role) {
        java.util.Objects.requireNonNull(role, "권한은 비어 있을 수 없습니다.");
    }

    public boolean isSameRole(Role role) {
        return this.role == role;
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

}