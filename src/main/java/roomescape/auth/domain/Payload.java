package roomescape.auth.domain;

import roomescape.member.domain.Member;
import roomescape.member.domain.Role;

public record Payload(Long memberId, Role role) {

    public static Payload from(Member member) {
        return new Payload(member.id(), member.role());
    }

    public static Payload from(String memberIdExpression, String roleExpression) {
        return new Payload(
                parseMemberId(memberIdExpression),
                Role.getByExpression(roleExpression));
    }

    public Payload {
        if (memberId == null) {
            throw new IllegalArgumentException("[ERROR] memberId가 빈 값이어서는 안 됩니다.");
        }
        if (role == null) {
            throw new IllegalArgumentException("[ERROR] role이 빈 값이어서는 안 됩니다.");
        }
    }

    public String getMemberIdExpression() {
        return String.valueOf(memberId);
    }

    public String getRoleExpression() {
        return role.getExpression();
    }

    public boolean isAuthorizedFor(Role requiredRole) {
        return role.isAuthorizedFor(requiredRole);
    }

    private static Long parseMemberId(String expression) {
        try {
            return Long.valueOf(expression);
        }
        catch (NumberFormatException e) {
            throw new IllegalArgumentException("[ERROR] memberId가 숫자 형식이 아닙니다.");
        }
    }
}
