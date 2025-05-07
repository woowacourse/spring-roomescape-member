package roomescape.domain;

import java.util.Objects;

public class User {

    private final int MAX_LENGTH = 255;

    private final Long id;
    private final String username;
    private final String email;
    private final String password;

    public User(Long id, String username, String email, String password) {
        validateName(username);
        validateEmail(email);
        validatePassword(password);

        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public User createWithoutId(String username, String email, String password) {
        return new User(null, username, email, password);
    }

    private void validateName(final String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("[ERROR] 유저 이름은 1글자 이상으로 이루어져야 합니다. ");
        }

        if(name.length() > MAX_LENGTH){
            throw new IllegalArgumentException("[ERROR] 유저 이름은 255자를 초과할 수 없습니다. 이름 길이 : " + name.length());
        }
    }

    private void validateEmail(final String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("[ERROR] 이메일은 1글자 이상으로 이루어져야 합니다. ");
        }

        if(password.length() > MAX_LENGTH){
            throw new IllegalArgumentException("[ERROR] 이메일은 255자를 초과할 수 없습니다. 이름 길이 : " + email.length());
        }
    }

    private void validatePassword(final String password) {
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("[ERROR] 비밀번호는 1글자 이상으로 이루어져야 합니다. ");
        }

        if(password.length() > MAX_LENGTH){
            throw new IllegalArgumentException("[ERROR] 비밀번호는 255자를 초과할 수 없습니다. 이름 길이 : " + password.length());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(username, user.username) && Objects.equals(email, user.email) && Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, email, password);
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
