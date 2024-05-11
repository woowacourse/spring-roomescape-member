package roomescape.domain;

import roomescape.auth.AuthException;

import java.util.Arrays;

public enum Role {
    USER("user"),
    ADMIN("admin");

    private final String name;

    Role(String name) {
        this.name = name;
    }

    public static Role findRole(String roleName) {
        return Arrays.stream(values())
                .filter(role -> role.name.equals(roleName))
                .findFirst()
                .orElseThrow(() -> new AuthException("일치하는 role이 없습니다."));
    }
}
