package roomescape.dto.reservation;

import java.time.LocalDate;
import java.util.List;
import roomescape.dto.reservationtime.ReservationTimeResponse;
import roomescape.dto.theme.ThemeResponse;
import roomescape.entity.ReservationEntity;

public record ReservationResponse(Long id, String name, LocalDate date, ReservationTimeResponse time,
                                  ThemeResponse theme) {
    public static ReservationResponse from(ReservationEntity reservationEntity) {
        return new ReservationResponse(reservationEntity.id(), reservationEntity.name(), reservationEntity.date(),
                ReservationTimeResponse.from(reservationEntity.time()), ThemeResponse.from(reservationEntity.theme()));
    }

    public static List<ReservationResponse> from(List<ReservationEntity> reservations) {
        return reservations.stream()
                .map(ReservationResponse::from)
                .toList();
    }
}
