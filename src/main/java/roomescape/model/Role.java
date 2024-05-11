package roomescape.model;

import java.util.Arrays;

public enum Role {
    ADMIN,
    USER;

    public static Role from(final String name) {
        return Arrays.stream(values())
                .filter(role -> role.name().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("존재하지 않는 역할입니다. (%s)", name)));
    }
}
