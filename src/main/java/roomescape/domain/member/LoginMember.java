package roomescape.domain.member;

import roomescape.exception.custom.AuthorizationException;

public class LoginMember {

    private final long id;
    private final String role;
    private final String name;
    private final String email;
    private final String password;

    public LoginMember(final long id,
                       final String role,
                       final String name,
                       final String email,
                       final String password) {
        this.id = id;
        this.role = role;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public void validateRightPassword(String inputPassword) {
        if (!password.equals(inputPassword)) {
            throw new AuthorizationException("비밀 번호가 옳지 않습니다");
        }
    }

    public long getId() {
        return id;
    }

    public String getRole() {
        return role;
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
}
