package roomescape.dto.member.response;

import roomescape.domain.member.Member;
import roomescape.domain.member.MemberRole;

public record MemberResponse(

        Long id,
        String name,
        String email,
        MemberRole role
) {

    public static MemberResponse from(final Member member) {
        return new MemberResponse(
                member.getId(),
                member.getName(),
                member.getEmail(),
                member.getRole()
        );
    }
}
