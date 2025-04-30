package roomescape.reservation.application.usecase;

import roomescape.reservation.domain.Reservation;
import roomescape.theme.domain.ThemeId;
import roomescape.time.domain.ReservationTimeId;

import java.time.LocalDate;
import java.util.List;

public interface ReservationQueryUseCase {

    List<Reservation> getAll();

    boolean existsByTimeId(ReservationTimeId timeId);

    boolean existsByParams(LocalDate date, ReservationTimeId timeId, ThemeId themeId);
}
