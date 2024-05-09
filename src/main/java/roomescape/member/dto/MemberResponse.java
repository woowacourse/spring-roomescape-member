package roomescape.member.dto;

import roomescape.member.domain.Member;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.dto.ReservationTimeResponse;

public record MemberResponse(long id, String name) {
    public static MemberResponse from(Member member) {
        return new MemberResponse(member.getId(), member.getName());
    }
}
