package roomescape.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.model.Reservation;
import roomescape.model.ReservationTime;

public class FakeReservationDao extends ReservationDao {

    private final Map<Long, Reservation> database = new HashMap<>();
    private final AtomicLong nextId = new AtomicLong(1L);

    public FakeReservationDao(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public Long saveReservation(Reservation reservation) {
        Long id = nextId.getAndIncrement();
        Reservation savedReservation = new Reservation(
                id,
                reservation.getName(),
                reservation.getDate(),
                new ReservationTime(reservation.getTimeId())
        );
        database.put(id, savedReservation);
        return id;
    }

    @Override
    public List<Reservation> findAll() {
        return new ArrayList<>(database.values());
    }

    @Override
    public void deleteById(Long id) {
        database.remove(id);
    }
}
