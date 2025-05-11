package roomescape.auth.domain;

import roomescape.member.domain.Member;

public record Payload(String memberId, String role) {

    public static Payload from(Member member) {
        return new Payload(String.valueOf(member.id()), member.role().getExpression());
    }

    public Payload {
        if (memberId == null || memberId.isBlank()) {
            throw new IllegalArgumentException("[ERROR] memberId가 빈 값이어서는 안 됩니다.");
        }

        if (role == null || role.isBlank()) {
            throw new IllegalArgumentException("[ERROR] role이 빈 값이어서는 안 됩니다.");
        }
    }
}
