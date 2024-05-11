package roomescape.service.dto;

import roomescape.domain.MemberInfo;
import roomescape.domain.Reservation;

public class MemberResponse {

    private final long id;
    private final String name;

    public MemberResponse(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public MemberResponse(MemberInfo memberInfo) {
        this(memberInfo.getId(), memberInfo.getName());
    }

    public MemberResponse(LoginMember member) {
        this(member.getId(), member.getName());
    }

    public MemberResponse(Reservation reservation) {
        this(reservation.getMemberId(), reservation.getMemberName());
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
