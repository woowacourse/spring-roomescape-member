package roomescape.member.domain;

public class Member {

    private final Long id;
    private final String name;
    private final String email;
    private final String password;

    public Member(Long id, String name, String email, String password) {
        validateId(id);
        validateName(name);
        validateEmail(email);
        validatePassword(password);
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    private void validateId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("회원 ID가 없습니다.");
        }
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("회원 이름이 없습니다.");
        }
    }

    private void validateEmail(String loginId) {
        if (loginId == null || loginId.isBlank()) {
            throw new IllegalArgumentException("회원 이메일이 없습니다.");
        }
    }

    private void validatePassword(String password) {
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("회원 비밀번호가 없습니다.");
        }
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

    @Override
    public String toString() {
        return "Member{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", email='" + email + '\'' +
            ", password='" + password + '\'' +
            '}';
    }
}
