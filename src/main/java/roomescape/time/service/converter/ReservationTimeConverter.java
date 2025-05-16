package roomescape.time.service.converter;

import roomescape.time.service.dto.CreateReservationTimeServiceRequest;
import roomescape.time.domain.ReservationTime;
import roomescape.time.controller.dto.ReservationTimeWebResponse;

import java.util.List;

public class ReservationTimeConverter {

    public static ReservationTime toDomain(final CreateReservationTimeServiceRequest request) {
        return ReservationTime.withoutId(
                request.startAt());
    }

    public static ReservationTimeWebResponse toDto(final ReservationTime reservationTime) {
        return new ReservationTimeWebResponse(
                reservationTime.getId().getValue(),
                reservationTime.getValue());
    }

    public static List<ReservationTimeWebResponse> toDto(final List<ReservationTime> reservationTimes) {
        return reservationTimes.stream()
                .map(ReservationTimeConverter::toDto)
                .toList();
    }
}
