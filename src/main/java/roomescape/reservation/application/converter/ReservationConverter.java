package roomescape.reservation.application.converter;

import roomescape.reservation.application.dto.CreateReservationServiceRequest;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationDate;
import roomescape.reservation.domain.ReservationId;
import roomescape.reservation.domain.ReserverName;
import roomescape.reservation.infrastructure.entity.ReservationEntity;
import roomescape.reservation.ui.dto.ReservationResponse;
import roomescape.time.application.converter.ReservationTimeConverter;
import roomescape.time.domain.ReservationTime;

import java.util.List;

public class ReservationConverter {

    public static Reservation toDomain(final ReservationEntity reservationEntity) {
        return Reservation.withId(
                ReservationId.from(reservationEntity.getId()),
                ReserverName.from(reservationEntity.getName()),
                ReservationDate.from(reservationEntity.getDate().toLocalDate()),
                ReservationTimeConverter.toDomain(reservationEntity.getTime()));
    }

    public static Reservation toDomain(final CreateReservationServiceRequest request,
                                       final ReservationTime time) {
        return Reservation.withoutId(
                ReserverName.from(request.name()),
                ReservationDate.from(request.date()),
                time);
    }

    public static ReservationResponse toDto(final Reservation reservation) {
        return new ReservationResponse(
                reservation.getId().getValue(),
                reservation.getName().getValue(),
                reservation.getDate().getValue(),
                ReservationTimeConverter.toDto(reservation.getTime()));
    }

    public static List<ReservationResponse> toDto(final List<Reservation> reservations) {
        return reservations.stream()
                .map(ReservationConverter::toDto)
                .toList();
    }
}
