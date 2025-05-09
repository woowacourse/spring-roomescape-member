package roomescape.member.domain;

public class Member {
    private final Long id;
    private final String name;
    private final String email;
    private final String password;

    public Member(Long id, String name, String email, String passWord) {
        validateName(name);
        validateEmail(email);
        validatePassword(passWord);
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = passWord;
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("[ERROR] 이름은 공백이 될 수 없습니다.");
        }
    }

    private void validateEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("[ERROR] 이메일은 공백이 될 수 없습니다.");
        }
    }

    private void validatePassword(String password) {
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("[ERROR] 비밀번호는 공백이 될 수 없습니다.");
        }
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
