package roomescape.domain;

import roomescape.exception.LoginFailedException;

public class Member {

    private final Long id;
    private final String name;
    private final String email;
    private final String password;

    public Member(Long id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public Member withId(Long id) {
        return new Member(id, name, email, password);
    }

    public void validatePassword(String password) {
        if (!this.password.equals(password)) {
            throw new LoginFailedException();
        }
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
