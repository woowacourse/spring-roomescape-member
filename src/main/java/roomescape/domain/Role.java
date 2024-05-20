package roomescape.domain;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Role {
    ADMIN,
    USER,
    GUEST;

    private static final Map<String, Role> ROLES = Stream.of(values())
            .collect(Collectors.toMap(Enum::name, role -> role));

    public static Role from(String value) {
        String uppercaseValue = value.toUpperCase();
        if (!ROLES.containsKey(uppercaseValue)) {
            throw new IllegalArgumentException("존재하지 않는 권한입니다: %s".formatted(value));
        }
        return ROLES.get(uppercaseValue);
    }

    public boolean isAdmin() {
        return this == ADMIN;
    }

    public String getName() {
        return this.name();
    }
}
