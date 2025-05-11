package roomescape.domain.auth.entity;

import java.util.Arrays;
import lombok.Getter;

@Getter
public enum Roles {
    USER("ROLE_USER"), ADMIN("ROLE_ADMIN");

    private final String roleName;

    Roles(final String roleName) {
        this.roleName = roleName;
    }

    public static Roles from(final String role) {
        return Arrays.stream(values())
                .filter(r -> r.roleName.equals(role))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid role: " + role));
    }

    public boolean isNotAdmin() {
        return !isAdmin();
    }

    public boolean isAdmin() {
        return this == Roles.ADMIN;
    }
}
