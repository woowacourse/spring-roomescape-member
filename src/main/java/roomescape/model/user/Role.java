package roomescape.model.user;

import java.util.Arrays;

public enum Role {
    ADMIN("admin"),
    USER("user");;
    private final String role;

    Role(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public static Role of(String role) {
        return Arrays.stream(values()).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 역할입니다. : " + role));
    }
}
