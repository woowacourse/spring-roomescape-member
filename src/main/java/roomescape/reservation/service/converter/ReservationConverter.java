package roomescape.reservation.service.converter;

import roomescape.member.domain.Member;
import roomescape.member.service.MemberConverter;
import roomescape.reservation.service.dto.AvailableReservationTimeServiceResponse;
import roomescape.reservation.service.dto.CreateReservationServiceRequest;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationDate;
import roomescape.reservation.controller.dto.AvailableReservationTimeWebResponse;
import roomescape.reservation.controller.dto.ReservationWebResponse;
import roomescape.theme.service.converter.ThemeConverter;
import roomescape.theme.domain.Theme;
import roomescape.time.service.converter.ReservationTimeConverter;
import roomescape.time.domain.ReservationTime;

import java.util.List;

public class ReservationConverter {

    public static Reservation toDomain(final CreateReservationServiceRequest request,
                                       final Member member,
                                       final ReservationTime time,
                                       final Theme theme) {
        return Reservation.withoutId(
                member,
                ReservationDate.from(request.date()),
                time,
                theme);
    }

    public static ReservationWebResponse toDto(final Reservation reservation) {
        return new ReservationWebResponse(
                reservation.getId().getValue(),
                MemberConverter.toDto(reservation.getMember()),
                reservation.getDate().getValue(),
                ReservationTimeConverter.toDto(reservation.getTime()),
                ThemeConverter.toDto(reservation.getTheme()));
    }

    public static List<ReservationWebResponse> toDto(final List<Reservation> reservations) {
        return reservations.stream()
                .map(ReservationConverter::toDto)
                .toList();
    }

    public static AvailableReservationTimeWebResponse toWebDto(final AvailableReservationTimeServiceResponse availableReservationTimeServiceResponse) {
        return new AvailableReservationTimeWebResponse(
                availableReservationTimeServiceResponse.startAt(),
                availableReservationTimeServiceResponse.timeId(),
                availableReservationTimeServiceResponse.isBooked()
        );
    }
}
