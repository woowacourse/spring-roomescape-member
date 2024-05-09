package roomescape.domain.member.mapper;

import roomescape.domain.member.domain.ReservationMember;
import roomescape.domain.member.dto.MemberResponse;

public class MemberMapper {

    public MemberResponse mapToResponse(ReservationMember member) {
        return new MemberResponse(member.getId(), member.getName());
    }
}
