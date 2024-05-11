package roomescape.domain;

import java.util.Arrays;

public enum Role {
    ADMIN("ADMIN"),
    USER("USER");

    private final String role;

    Role(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public static Role findRole(String role) {
        return Arrays.stream(values()).filter(r -> r.getRole().equals(role))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("해당하는 Role 이 존재하지 않습니다."));
    }
}
