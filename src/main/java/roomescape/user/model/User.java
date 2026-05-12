package roomescape.user.model;

public class User {

    private Long id;
    private String name;
    private Role role;

    public User(){}

    public User(String name, Role role) {
        this(null, name, role);
    }

    public User(Long id, String name, Role role) {
        validateName(name);
        validateRole(role);
        this.id = id;
        this.name = name;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Role getRole() {
        return role;
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("사용자 이름은 필수이며, 공백일 수 없습니다.");
        }
    }

    private void validateRole(Role role) {
        if (role == null) {
            throw new IllegalArgumentException("사용자 권한은 필수입니다.");
        }
    }
}
