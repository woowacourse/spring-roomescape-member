package roomescape.member.domain;

import java.util.EnumSet;
import roomescape.member.application.RoleNotFoundException;

public enum Role {

    ADMIN("admin"),
    MEMBER("member");

    private final String expression;

    Role(String expression) {
        this.expression = expression;
    }

    public static Role getByExpression(String expression) {
        return EnumSet.allOf(Role.class).stream()
                .filter(role -> role.getExpression().equalsIgnoreCase(expression))
                .findAny()
                .orElseThrow(RoleNotFoundException::new);
    }

    public String getExpression() {
        return expression;
    }

    public boolean isAuthorizedFor(Role requiredRole) {
        return this == requiredRole;
    }
}
