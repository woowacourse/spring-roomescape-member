package roomescape.admin.domain;

public class Admin {

    private final Long id;
    private final String name;
    private final String loginId;
    private final String password;

    public Admin(Long id, String name, String loginId, String password) {
        validateId(id);
        validateName(name);
        validateLoginId(loginId);
        validatePassword(password);
        this.id = id;
        this.name = name;
        this.loginId = loginId;
        this.password = password;
    }

    public Admin(String name, String loginId, String password) {
        validateName(name);
        validateLoginId(loginId);
        validatePassword(password);
        this.id = null;
        this.name = name;
        this.loginId = loginId;
        this.password = password;
    }

    private void validateId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("관리자 ID가 없습니다.");
        }
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("관리자 이름이 없습니다.");
        }
    }

    private void validateLoginId(String loginId) {
        if (loginId == null || loginId.isBlank()) {
            throw new IllegalArgumentException("관리자 로그인 ID가 없습니다.");
        }
    }

    private void validatePassword(String password) {
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("관리자 비밀번호가 없습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLoginId() {
        return loginId;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "Admin{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", loginId='" + loginId + '\'' +
            ", password='" + password + '\'' +
            '}';
    }
}
