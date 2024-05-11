package roomescape.member.dto.response;

import roomescape.member.model.Member;

public record FindMemberOfReservationResponse(Long id, String name) {

    public static FindMemberOfReservationResponse of(Member member) {
        return new FindMemberOfReservationResponse(member.getId(), member.getName());
    }
}
