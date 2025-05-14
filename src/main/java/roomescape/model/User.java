package roomescape.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class User {
    private final Long id;
    private final UserName name;
    private final String email;
    private final String password;
    private final Role role;

    public User(Long id, UserName name, String email, String password, Role role) {
        this.id = id;
        this.name = name;
        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("유효하지 않은 이메일 형식입니다.");
        }
        this.email = email;
        this.password = password;
        this.role = role;
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public Long getId() {
        return id;
    }

    public UserName getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getNameValue() {
        return name.getName();
    }

    public Role getRole() {
        return role;
    }
}
