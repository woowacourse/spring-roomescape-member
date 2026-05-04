package roomescape.service.fake;

import org.w3c.dom.stylesheets.LinkStyle;
import roomescape.dao.ReservationDao;
import roomescape.dao.vo.ReservationRow;
import roomescape.dao.vo.ReservationRows;
import roomescape.domain.Reservation;
import roomescape.domain.Time;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FakeReservationDao implements ReservationDao {

    private final Map<Long, Reservation> store = new HashMap<>();
    private long sequence = 0L;


    @Override
    public List<Reservation> findAll() {
        return store.values().stream().toList();
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        Reservation reservation = store.get(id);

        if (reservation == null) {
            return Optional.empty();
        }

        return Optional.of(reservation);
    }

    @Override
    public Long insert(Reservation reservation) {
        Long id = ++sequence;
        store.put(id, new Reservation(
                id,
                reservation.getName(),
                reservation.getDate(),
                reservation.getTime()));
        return id;

    }

    @Override
    public int delete(Long id) {
        Reservation remove = store.remove(id);

        if (remove == null) {
            return 0;
        }
        return 1;
    }
}
