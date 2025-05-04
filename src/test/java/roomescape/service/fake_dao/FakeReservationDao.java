package roomescape.service.fake_dao;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import roomescape.dao.ReservationDao;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

public class FakeReservationDao implements ReservationDao {

    private final List<Reservation> fakeMemory = new ArrayList<>();
    private Long id = 1L;

    @Override
    public List<Reservation> findAll() {
        return Collections.unmodifiableList(fakeMemory);
    }

    @Override
    public Long save(Reservation reservation) {
        long reservationId = id++;
        fakeMemory.add(reservation.copyWithId(reservationId));
        return reservationId;
    }

    @Override
    public void deleteById(Long id) {
        fakeMemory.removeIf(reservation -> reservation.getId() == id);
    }

    @Override
    public Boolean existByTimeId(Long timeId) {
        return true;
    }

    @Override
    public Boolean existByDateAndTimeId(LocalDate date, Long timeId) {
        return null;
    }

    @Override
    public Boolean existByDateAndTimeIdAndThemeId(LocalDate date, ReservationTime time, Long themeId) {
        return null;
    }

    @Override
    public List<Long> findTop10ByBetweenDates(LocalDate start, LocalDate end) {
        return List.of();
    }
}
