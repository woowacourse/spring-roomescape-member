package roomescape.dto.member;

import roomescape.domain.member.Member;

public record ReservationMemberResponse(Long id, String name) {

    public static ReservationMemberResponse of(Member member) {
        return new ReservationMemberResponse(member.getId(), member.getName().getValue());
    }
}
