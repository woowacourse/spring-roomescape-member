package roomescape.reservationtime.repository;

import java.util.Map;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import roomescape.reservationtime.dto.AvailableTime;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.dao.ReservationTimeDao;

@Repository
public class JdbcReservationTimeRepository implements ReservationTimeRepository {
    private final ReservationTimeDao reservationTimeDao;

    public JdbcReservationTimeRepository(ReservationTimeDao reservationTimeDao) {
        this.reservationTimeDao = reservationTimeDao;
    }

    @Override
    public List<ReservationTime> findAll() {
        return reservationTimeDao.findAll().stream()
                .map(this::mapToReservationTime)
                .toList();
    }

    @Override
    public ReservationTime save(LocalTime startAt) {
        long id = reservationTimeDao.save(startAt);
        return new ReservationTime(id, startAt);
    }

    @Override
    public void delete(long id) {
        reservationTimeDao.delete(id);
    }

    @Override
    public Optional<ReservationTime> findById(long id) {
        return reservationTimeDao.findById(id)
                .map(this::mapToReservationTime);
    }

    private ReservationTime mapToReservationTime(Map<String, Object> row) {
        return new ReservationTime(
                (Long) row.get("id"),
                ((java.sql.Time) row.get("start_at")).toLocalTime()
        );
    }

    @Override
    public List<AvailableTime> findAvailableTimes(Long themeId, LocalDate date) {
        return reservationTimeDao.findAvailableTimes(themeId, date).stream()
                .map(row -> new AvailableTime(
                        (Long) row.get("timeId"),
                        ((java.sql.Time) row.get("time")).toLocalTime(),
                        (Boolean) row.get("isAvailable")
                ))
                .toList();
    }
}
