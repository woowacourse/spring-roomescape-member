package roomescape.domain;

import java.util.Arrays;

public enum Role {

    ADMIN("ADMIN"),
    USER("USER");

    private final String name;

    Role(String name) {
        this.name = name;
    }

    public static Role of(String roleName) {
        return Arrays.stream(values())
                .filter(role -> role.name.equalsIgnoreCase(roleName))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("역할을 찾을 수 없습니다."));
    }
}
