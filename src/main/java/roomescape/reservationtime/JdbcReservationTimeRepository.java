package roomescape.reservationtime;

import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcReservationTimeRepository implements ReservationTimeRepository {
    private final ReservationTimeDao reservationTimeDao;

    public JdbcReservationTimeRepository(ReservationTimeDao reservationTimeDao) {
        this.reservationTimeDao = reservationTimeDao;
    }

    @Override
    public List<ReservationTime> findAll() {
        return reservationTimeDao.findAll().stream()
                .map(row -> new ReservationTime(
                        (Long) row.get("id"),
                        ((java.sql.Time) row.get("start_at")).toLocalTime()
                ))
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
                .map(row -> new ReservationTime(
                        (Long) row.get("id"),
                        ((java.sql.Time) row.get("start_at")).toLocalTime()
                ));
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
