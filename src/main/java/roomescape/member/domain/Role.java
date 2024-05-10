package roomescape.member.domain;

import java.util.Arrays;

public enum Role {
    ADMIN,
    USER,
    ;

    public static Role findByName(String name) {
        return Arrays.stream(Role.values())
                .filter(role -> role.name().equals(name))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 역할입니다."));
    }

    public boolean isAdminRole() {
        return this.equals(ADMIN);
    }
}
