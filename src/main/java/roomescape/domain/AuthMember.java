package roomescape.domain;

public class AuthMember {

    private final Long id;
    private final String name;
    private final Role role;

    public AuthMember(Long id, String name, Role role) {
        this.id = id;
        this.name = name;
        this.role = role;
    }

    public boolean isNotAdmin() {
        return role.isNotAdmin();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
