package roomescape.domain;

import java.time.LocalDate;
import java.util.List;

public interface ReservationTimeRepository {

    List<ReservationTime> findAll();

    ReservationTime findById(Long id);

    ReservationTime save(ReservationTime reservationTime);

    void deleteById(Long id);

    List<ReservationTime> findByReservationDateAndThemeId(LocalDate date, Long themeId);
}
