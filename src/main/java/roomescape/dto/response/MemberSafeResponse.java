package roomescape.dto.response;

import roomescape.entity.Member;
import roomescape.entity.MemberRole;

public record MemberSafeResponse(
        long id,
        String name,
        String email,
        MemberRole role
) {

    public MemberSafeResponse(Member member) {
        this(
                member.getId(),
                member.getName(),
                member.getEmail(),
                member.getRole()
        );
    }
}
