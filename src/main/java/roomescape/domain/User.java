package roomescape.domain;

public class User {

    private final Long id;
    private final String name;
    private final String email;

    public User(Long id, String name, String email) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("사용자 이름은 필수입니다.");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("사용자 이메일은 필수입니다.");
        }
        this.id = id;
        this.name = name;
        this.email = email;
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
}
