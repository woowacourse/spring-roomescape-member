package roomescape.reservationtime.repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import roomescape.reservationtime.domain.ReservationTime;

public interface ReservationTimeRepository {

    List<ReservationTime> findAll();

    List<ReservationTime> findAllByThemeId(long themeId);

    Optional<ReservationTime> findByTimeIdAndThemeId(long timeId, long themeId);

    void deleteByTimeIdAndThemeId(long timeId, long themeId);

    ReservationTime save(ReservationTime reservationTime);

    boolean existsByStartAtAndThemeId(final LocalTime startAt, final long themeId);

}
