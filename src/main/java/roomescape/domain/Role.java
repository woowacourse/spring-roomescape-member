package roomescape.domain;

import java.util.Arrays;

public enum Role {
    USER("USER"),
    ADMIN("ADMIN"),
    ;

    private final String role;

    Role(String role) {
        this.role = role;
    }

    public static Role from(String role) {
        return Arrays.stream(values())
                .filter(r -> r.getRole().equals(role))
                .findFirst()
                .orElse(null);
    }

    public String getRole() {
        return role;
    }
}
