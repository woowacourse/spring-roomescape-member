package roomescape.model;

public class User {
    private final Long id;
    private final UserName name;
    private final String password;

    public User(final Long id, final UserName name, final String password) {
        validateNotNull(name, password);
        this.id = id;
        this.name = name;
        this.password = password;
    }

    private void validateNotNull(final UserName name, final String password) {
        if (name == null || password == null) {
            throw new IllegalArgumentException("유저 생성 시 이름, 비밀번호는 필수입니다.");
        }
    }


}
