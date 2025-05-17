package roomescape.model;

import java.util.Arrays;
import roomescape.exception.RoomescapeException;

public enum Role {

    ADMIN, USER;

    public static Role findByName(final String name) {
        return Arrays.stream(Role.values())
                .filter(role -> role.name().equals(name))
                .findAny()
                .orElseThrow(() -> new RoomescapeException("존재하지 않는 ROLE입니다."));
    }
}
