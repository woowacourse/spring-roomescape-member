package roomescape.domain;

import roomescape.common.exception.AuthorizationException;

import java.util.Arrays;

public enum MemberRole {
    ADMIN("ADMIN"),
    USER("USER");

    private final String name;

    MemberRole(String name) {
        this.name = name;
    }

    public static MemberRole from(String value) {
        return Arrays.stream(MemberRole.values())
                .filter(role -> role.name.equals(value))
                .findFirst()
                .orElseThrow(() -> new AuthorizationException("존재하지 않는 인증 정보입니다"));
    }
}
