package roomescape.reservation.mapper;

import roomescape.admin.dto.ReservationSaveRequest;
import roomescape.member.domain.ReservationMember;
import roomescape.member.dto.MemberResponse;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.theme.theme.domain.Theme;

public class ReservationMapper {

    public ReservationResponse mapToResponse(Reservation reservation) {
        MemberResponse member = new MemberResponse(reservation.getMemberId(), reservation.getName());
        return new ReservationResponse(reservation.getId(), member, reservation.getDate(), reservation.getTime(), reservation.getTheme());
    }

    public ReservationResponse mapToResponse(Long id, Reservation reservation) {
        MemberResponse member = new MemberResponse(reservation.getId(), reservation.getName());
        return new ReservationResponse(id, member, reservation.getDate(), reservation.getTime(), reservation.getTheme());
    }

    public Reservation mapToReservation(ReservationSaveRequest request, ReservationMember member, ReservationTime reservationTime, Theme theme) {
        return new Reservation(null, member, request.date(), reservationTime, theme);
    }
}
