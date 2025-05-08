package roomescape.member.domain;

import java.util.Arrays;

public enum UserRole {

    ADMIN("어드민"),
    MEMBER("멤버"),
    GUEST("게스트"),
    ;

    private final String roleName;

    UserRole(String roleName) {
        this.roleName = roleName;
    }

    public static UserRole from(final String roleName) {
        return Arrays.stream(values())
                .filter(userRole -> userRole.roleName.equals(roleName))
                .findFirst()
                .orElseGet(() -> UserRole.GUEST);
    }

    public String getRoleName() {
        return roleName;
    }
}
