package roomescape.reservationTime;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ReservationTimeRepository {

    Long save(ReservationTime reservationTime);

    ReservationTime findById(Long id);
    List<ReservationTime> findAll();
    List<ReservationTime> findAllByThemeIdAndDate(Long themeId, LocalDate date);

    void deleteById(Long id);

    Boolean existsByStartAt(LocalTime startAt);
}
