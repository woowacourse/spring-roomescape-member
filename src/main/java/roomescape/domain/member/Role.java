package roomescape.domain.member;

import java.util.Arrays;

public enum Role {
    ADMIN,
    MEMBER;

    public static Role from(String role) {
        return Arrays.stream(Role.values())
                .filter(element -> element.name().equals(role))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 존재하지 않는 역할입니다."));
    }
}
