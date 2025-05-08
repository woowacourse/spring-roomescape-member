package roomescape.domain.auth.entity;

import java.util.Arrays;
import lombok.Getter;

@Getter
public enum Roles {
    USER("ROLE_USER"), ADMIN("ROLE_ADMIN");

    private final String role;

    Roles(final String role) {
        this.role = role;
    }

    public static Roles from(final String role) {
        return Arrays.stream(values())
                .filter(r -> r.role.equals(role))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid role: " + role));
    }

    public boolean isNotAdmin() {
        return this != Roles.ADMIN;
    }
}
