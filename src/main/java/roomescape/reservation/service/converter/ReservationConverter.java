package roomescape.reservation.service.converter;

import roomescape.reservation.service.dto.AvailableReservationTimeServiceResponse;
import roomescape.reservation.service.dto.CreateReservationServiceRequest;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationDate;
import roomescape.reservation.domain.ReservationId;
import roomescape.reservation.domain.ReserverName;
import roomescape.reservation.repository.entity.ReservationEntity;
import roomescape.reservation.controller.dto.AvailableReservationTimeWebResponse;
import roomescape.reservation.controller.dto.ReservationResponse;
import roomescape.theme.service.converter.ThemeConverter;
import roomescape.theme.domain.Theme;
import roomescape.time.service.converter.ReservationTimeConverter;
import roomescape.time.domain.ReservationTime;

import java.util.List;

public class ReservationConverter {

    public static Reservation toDomain(final ReservationEntity reservationEntity) {
        return Reservation.withId(
                ReservationId.from(reservationEntity.getId()),
                ReserverName.from(reservationEntity.getName()),
                ReservationDate.from(reservationEntity.getDate().toLocalDate()),
                ReservationTimeConverter.toDomain(reservationEntity.getTime()),
                ThemeConverter.toDomain(reservationEntity.getTheme()));
    }

    public static Reservation toDomain(final CreateReservationServiceRequest request,
                                       final ReservationTime time,
                                       final Theme theme) {
        return Reservation.withoutId(
                ReserverName.from(request.name()),
                ReservationDate.from(request.date()),
                time,
                theme);
    }

    public static ReservationResponse toDto(final Reservation reservation) {
        return new ReservationResponse(
                reservation.getId().getValue(),
                reservation.getName().getValue(),
                reservation.getDate().getValue(),
                ReservationTimeConverter.toDto(reservation.getTime()),
                ThemeConverter.toDto(reservation.getTheme()));
    }

    public static List<ReservationResponse> toDto(final List<Reservation> reservations) {
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
