package roomescape.domain;

import java.util.Arrays;

public enum MemberRole {
    ADMIN,
    USER;

    public static MemberRole findByName(String role) {
        return Arrays.stream(MemberRole.values())
                .filter(r -> r.name().equals(role))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 역할입니다."));
    }
}
