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
}
