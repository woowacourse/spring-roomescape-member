package roomescape.domain.model;

import java.util.Arrays;

public enum Role {
    USER("user"),
    ADMIN("admin");

    private final String role;

    Role(final String role) {
        this.role = role;
    }

    public static Role fromName(String name) {
        return Arrays.stream(Role.values())
                .filter(role -> role.role.equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 해당 role은 존재하지 않습니다."));
    }

    public boolean isAdmin() {
        return this.equals(ADMIN);
    }

    public String getRole() {
        return role;
    }
}
