package roomescape.member.domain;

import java.util.Arrays;

public enum Role {
    ADMIN,
    USER;

    public static Role from(String value) {
        return Arrays.stream(Role.values())
                .filter(role -> role.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 권한입니다."));
    }

}
