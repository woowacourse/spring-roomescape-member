package roomescape.dto;

import roomescape.entity.Member;
import roomescape.entity.MemberRole;

public record MemberResponse(
        long id,
        String name,
        String email,
        MemberRole role
) {

    public MemberResponse(Member member) {
        this(
                member.getId(),
                member.getName(),
                member.getEmail(),
                member.getRole()
        );
    }
}
