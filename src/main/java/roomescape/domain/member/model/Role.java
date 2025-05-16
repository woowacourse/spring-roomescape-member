package roomescape.domain.member.model;

import java.util.Arrays;

public enum Role {

    ADMIN, USER;

    public static Role convertFrom(String role) {
        return Arrays.stream(Role.values())
            .filter(value -> value.name().equalsIgnoreCase(role))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 역할입니다."));
    }

}
