package roomescape.domain;

import java.util.Arrays;

public enum Role {
    ADMIN("admin"),
    USER("user");

    private final String expression;

    Role(final String expression) {
        this.expression = expression;
    }

    public static Role from(final String expression) {
        return Arrays.stream(values())
                .filter(role -> role.isExpressionMatch(expression))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("일치하는 Role이 없습니다."));
    }

    private boolean isExpressionMatch(final String expression) {
        return this.expression.equals(expression);
    }
}
