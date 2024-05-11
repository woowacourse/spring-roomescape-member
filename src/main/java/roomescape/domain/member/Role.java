package roomescape.domain.member;

import java.util.Arrays;

public enum Role {
    NONE("NONE"),
    MEMBER("MEMBER"),
    ADMIN("ADMIN");

    private final String name;

    Role(String name) {
        this.name = name;
    }

    public static Role of(String name) {
        return Arrays.stream(values())
                .filter(role -> name.equals(role.name))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 Role 입니다."));
    }

    public String getName() {
        return name;
    }
}
