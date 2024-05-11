package roomescape.domain.member;

import java.util.Arrays;

public enum Role {
    NONE("NONE", 0),
    MEMBER("MEMBER", 1),
    ADMIN("ADMIN", 2);

    private final String name;
    private final int authorityLevel;

    Role(String name, int authorityLevel) {
        this.name = name;
        this.authorityLevel = authorityLevel;
    }

    public static Role of(String name) {
        return Arrays.stream(values())
                .filter(role -> name.equals(role.name))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 Role 입니다."));
    }

    public boolean isHigherAuthority(Role otherRole) {
        return this.authorityLevel > otherRole.authorityLevel;
    }

    public String getName() {
        return name;
    }
}
