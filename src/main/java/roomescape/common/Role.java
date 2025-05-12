package roomescape.common;

import java.util.Arrays;
import java.util.Objects;
import roomescape.common.exception.NotFoundException;

public enum Role {
    USER("user"),
    ADMIN("admin");

    private final String memberRole;

    Role(String memberRole) {
        this.memberRole = memberRole;
    }

    public String getMemberRole() {
        return memberRole;
    }

    public static Role fromMemberRole(String findRole) {
        return Arrays.stream(Role.values())
                .filter(role -> Objects.equals(role.memberRole, findRole))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("존재하지 않는 role 입니다."));
    }
}
