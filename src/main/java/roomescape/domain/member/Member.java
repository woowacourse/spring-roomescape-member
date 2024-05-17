package roomescape.domain.member;

public class Member {

    private final Long id;
    private final String name;
    private final String email;
    private final String password;
    private final Role role;

    public Member(Long id, String name, String email, String password, Role role) {
        validateEmailAndPassword(email, password);
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    private void validateEmailAndPassword(String email, String password) {
        if (email.equals(password)) {
            throw new IllegalArgumentException("이메일과 비밀번호는 같을 수 없습니다.");
        }
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
