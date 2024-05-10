package roomescape.service.dto;

import roomescape.domain.MemberInfo;
import roomescape.domain.Reservation;

public class MemberResponseDto {

    private final long id;
    private final String name;

    public MemberResponseDto(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public MemberResponseDto(MemberInfo memberInfo) {
        this(memberInfo.getId(), memberInfo.getName());
    }

    public MemberResponseDto(LoginMember member) {
        this(member.getId(), member.getName());
    }

    public MemberResponseDto(Reservation reservation) {
        this(reservation.getMemberId(), reservation.getMemberName());
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
