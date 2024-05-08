package roomescape.dto;

import roomescape.domain.member.Member;

public record MemberPayload(
        String id,
        String name,
        String email
) {

    public static MemberPayload from(Member member) {
        return new MemberPayload(
                member.getId().toString(),
                member.getName(),
                member.getEmail()
        );
    }
}
