package roomescape.service.fake_dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import roomescape.dao.ReservationDao;
import roomescape.domain_entity.Id;
import roomescape.domain_entity.Reservation;
import roomescape.domain_entity.ReservationTime;

public class FakeReservationDao implements ReservationDao {

    private long id = 1L;
    private final List<Reservation> fakeMemory = new ArrayList<>();

    @Override
    public List<Reservation> findAll() {
        return Collections.unmodifiableList(fakeMemory);
    }

    @Override
    public long create(Reservation reservation) {
        long reservationId = id++;
        fakeMemory.add(reservation.copyWithId(new Id(reservationId)));
        return reservationId;
    }

    @Override
    public void deleteById(Id id) {
        fakeMemory.removeIf(reservation -> reservation.getId() == id.value());
    }
}
