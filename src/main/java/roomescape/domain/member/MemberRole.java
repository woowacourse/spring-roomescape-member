package roomescape.domain.member;

import java.util.Arrays;
import roomescape.exception.AuthorizationException;

public enum MemberRole {

    ADMIN("admin"),
    USER("user");

    private final String name;

    MemberRole(String name) {
        this.name = name;
    }

    public static MemberRole from(String input) {
        return Arrays.stream(values())
                .filter(role -> role.name.equals(input))
                .findFirst()
                .orElseThrow(() -> new AuthorizationException("존재하지 않는 권한입니다."));
    }

    public String getName() {
        return name;
    }
}
