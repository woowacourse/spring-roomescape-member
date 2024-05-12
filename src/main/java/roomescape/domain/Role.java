package roomescape.domain;

import java.util.Arrays;

public enum Role {
    ADMIN,
    USER,
    GUEST;

    public static Role from(String value) {
        return Arrays.stream(values())
                .filter(role -> role.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
