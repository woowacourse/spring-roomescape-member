package roomescape.dto;

import roomescape.domain.Member;
import roomescape.domain.Reservation;

public record MemberResponse(
        Long id,
        String name,
        String email,
        String password
) {

    public static MemberResponse from(final Member member) {
        return new MemberResponse(
                member.getId(),
                member.getNameString(),
                member.getEmail(),
                member.getPassword()
        );
    }
}
