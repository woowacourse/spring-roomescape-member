package roomescape.member.mapper;

import roomescape.member.domain.ReservationMember;
import roomescape.member.dto.MemberResponse;

public class MemberMapper {

    public MemberResponse mapToResponse(ReservationMember member) {
        return new MemberResponse(member.getId(), member.getName());
    }
}
