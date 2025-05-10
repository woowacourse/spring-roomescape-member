package roomescape.domain.member;

public class Member {

    private final Long id;
    private final String name;
    private final Role role;
    private final String email;
    private final String password;

    public Member(Long id, String name, String email, String password) {
        validate(name, email, password);
        this.id = id;
        this.role = Role.USER;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public Member(Long id, String name, Role role, String email, String password) {
        this.id = id;
        this.role = role;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    private void validate(String name, String email, String password) {
        validateName(name);
        validateEmail(email);
        validatePassword(password);
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("[ERROR] 이름에 공백을 입력할 수 없습니다.");
        }
    }

    private void validateEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("[ERROR] 이메일에 공백을 입력할 수 없습니다.");
        }

        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        if (!email.matches(emailRegex)) {
            throw new IllegalArgumentException("[ERROR] 유효하지 않은 이메일 형식입니다.");
        }
    }

    private void validatePassword(String password) {
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("[ERROR] 비밀번호에 공백을 입력할 수 없습니다.");
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

    public Role getRole() {
        return role;
    }
}
