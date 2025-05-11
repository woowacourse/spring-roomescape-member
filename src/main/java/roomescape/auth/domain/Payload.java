package roomescape.auth.domain;

import roomescape.member.domain.Member;

public record Payload(String memberId, String role) {
    // TODO : 검증로직 추가

    public static Payload from(Member member) {
        return new Payload(String.valueOf(member.id()), member.role().getExpression());
    }
}
