package roomescape.member;

import java.util.Arrays;
import roomescape.exception.UnAuthorizedException;

public enum Role {
    ADMIN, USER;

    public static Role of(final String name) {
        return Arrays.stream(Role.values())
                .filter(role -> role.name().equals(name))
                .findFirst()
                .orElseThrow(() -> new UnAuthorizedException("권한을 찾을 수 없습니다."));
    }
}
