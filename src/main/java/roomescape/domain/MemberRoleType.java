package roomescape.domain;

import java.util.Arrays;

public enum MemberRoleType {
    ADMIN("ADMIN"),
    MEMBER("MEMBER");

    private final String name;

    MemberRoleType(final String name) {
        this.name = name;
    }

    public static MemberRoleType from(String name) {
        return Arrays.stream(MemberRoleType.values())
                .filter(memberRoleType -> memberRoleType.name.equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 role 입니다"));
    }

    public String getName() {
        return name;
    }
}
