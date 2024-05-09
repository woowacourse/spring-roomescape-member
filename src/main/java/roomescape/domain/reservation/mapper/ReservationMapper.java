package roomescape.domain.reservation.mapper;

import roomescape.domain.admin.dto.AdminReservationSaveRequest;
import roomescape.domain.member.domain.ReservationMember;
import roomescape.domain.member.dto.MemberResponse;
import roomescape.domain.reservation.domain.Reservation;
import roomescape.domain.reservation.domain.ReservationTime;
import roomescape.domain.reservation.dto.ReservationResponse;
import roomescape.domain.reservation.dto.ReservationSaveRequest;
import roomescape.domain.theme.domain.Theme;

public class ReservationMapper {

    public ReservationResponse mapToResponse(Reservation reservation) {
        MemberResponse member = new MemberResponse(reservation.getId(), reservation.getName());
        return new ReservationResponse(reservation.getId(), member, reservation.getDate(), reservation.getTime(), reservation.getTheme());
    }

    public ReservationResponse mapToResponse(Long id, Reservation reservation) {
        MemberResponse member = new MemberResponse(reservation.getId(), reservation.getName());
        return new ReservationResponse(id, member, reservation.getDate(), reservation.getTime(), reservation.getTheme());
    }

    public Reservation mapToReservation(ReservationSaveRequest request, ReservationMember member, ReservationTime reservationTime, Theme theme) {
        return new Reservation(null, member, request.date(), reservationTime, theme);
    }

    public Reservation mapToReservation(AdminReservationSaveRequest request, ReservationMember member, ReservationTime reservationTime, Theme theme) {
        return new Reservation(null, member, request.date(), reservationTime, theme);
    }
}
