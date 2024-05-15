package roomescape.domain;

import java.util.Arrays;

public enum MemberRole {

    NORMAL, ADMIN;

    public static MemberRole from(String role) {
        return Arrays.stream(values())
                .filter(memberRole -> memberRole.name().equalsIgnoreCase(role))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 역할입니다."));
    }

    public boolean isAdmin() {
        return this == ADMIN;
    }
}
