package roomescape.member.domain;

public enum Role {
    USER(1L, "user"),
    ADMIN(2L, "admin");

    private final Long id;
    private final String name;

    Role(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static Role getRoleById(Long id) {
        for (Role role : Role.values()) {
            if (role.id.equals(id)) {
                return role;
            }
        }
        return null;
    }

    public Long getId() {
        return id;
    }
}
