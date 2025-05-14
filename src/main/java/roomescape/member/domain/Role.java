package roomescape.member.domain;

import java.util.Arrays;
import roomescape.member.exception.MemberRoleNotExistsException;

public enum Role {
    ADMIN("admin"),
    USER("user");

    private final String name;

    Role(String name) {
        this.name = name;
    }

    public static Role findBy(String name) {
        return Arrays.stream(values())
                .filter(role -> role.name.equals(name))
                .findAny()
                .orElseThrow(MemberRoleNotExistsException::new);
    }

    public String getName() {
        return name;
    }
}
