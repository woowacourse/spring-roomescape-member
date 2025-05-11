package roomescape.auth.domain;

import roomescape.member.domain.Member;
import roomescape.member.domain.Role;

public record Payload(String memberId, Role role) {

    public static Payload from(Member member) {
        return new Payload(String.valueOf(member.id()), member.role());
    }

    public static Payload of(String subject, String roleExpression) {
        return new Payload(subject, Role.getByExpression(roleExpression));
    }

    public Payload {
        if (memberId == null || memberId.isBlank()) {
            throw new IllegalArgumentException("[ERROR] memberId가 빈 값이어서는 안 됩니다.");
        }

        if (role == null) {
            throw new IllegalArgumentException("[ERROR] role이 빈 값이어서는 안 됩니다.");
        }
    }
}
