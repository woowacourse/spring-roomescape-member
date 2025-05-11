package roomescape.dto;

import roomescape.domain.Member;
import roomescape.domain.MemberRole;

public record MemberResponse(

        Long id,
        String name,
        String email,
        MemberRole role
) {

    public static MemberResponse from(Member member) {
        return new MemberResponse(
                member.getId(),
                member.getName(),
                member.getEmail(),
                member.getRole()
        );
    }
}
