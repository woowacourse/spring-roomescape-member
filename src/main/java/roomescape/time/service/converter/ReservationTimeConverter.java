package roomescape.time.service.converter;

import roomescape.time.service.dto.CreateReservationTimeServiceRequest;
import roomescape.time.domain.ReservationTime;
import roomescape.time.domain.ReservationTimeId;
import roomescape.time.repository.entity.ReservationTimeEntity;
import roomescape.time.controller.dto.ReservationTimeResponse;

import java.util.List;

public class ReservationTimeConverter {

    public static ReservationTime toDomain(final CreateReservationTimeServiceRequest request) {
        return ReservationTime.withoutId(
                request.startAt());
    }

    public static ReservationTime toDomain(final ReservationTimeEntity entity) {
        return ReservationTime.withId(
                ReservationTimeId.from(entity.getId()),
                entity.getTime().toLocalTime());
    }

    public static ReservationTimeResponse toDto(final ReservationTime reservationTime) {
        return new ReservationTimeResponse(
                reservationTime.getId().getValue(),
                reservationTime.getValue());
    }

    public static List<ReservationTimeResponse> toDto(final List<ReservationTime> reservationTimes) {
        return reservationTimes.stream()
                .map(ReservationTimeConverter::toDto)
                .toList();
    }
}
