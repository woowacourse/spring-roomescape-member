package roomescape.domain;

public class Member {

    private final String name;
    private final String email;
    private final String password;

    public Member(String name, String email, String password) {
        validateName(name);

        this.name = name;
        this.email = email;
        this.password = password;
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("[ERROR] 사용자의 이름은 1글자 이상으로 이루어져야 합니다. ");
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
}
