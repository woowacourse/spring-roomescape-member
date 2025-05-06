package roomescape.service.fake_dao;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import roomescape.dao.ReservationDao;
import roomescape.entity.Reservation;
import roomescape.entity.ReservationTime;

public class FakeReservationDao implements ReservationDao {

    private final List<Reservation> fakeMemory = new ArrayList<>();
    private Long id = 1L;

    @Override
    public List<Reservation> findAll() {
        return Collections.unmodifiableList(fakeMemory);
    }

    @Override
    public Long create(Reservation reservation) {
        long reservationId = id++;
        fakeMemory.add(reservation.copyWithId(reservationId));
        return reservationId;
    }

    @Override
    public void deleteById(Long id) {
        fakeMemory.removeIf(reservation -> Objects.equals(reservation.getId(), id));
    }

    @Override
    public boolean existByTimeId(Long timeId) {
        return false;
    }

    @Override
    public boolean existBySameDateTime(Reservation reservation) {
        return false;
    }

    @Override
    public boolean existByThemeId(Long themeId) {
        return false;
    }

    @Override
    public boolean existByDateTimeAndTheme(LocalDate date, ReservationTime time, Long themeId) {
        return false;
    }

    @Override
    public List<Long> findMostReservedThemeIdsBetween(LocalDate startDate, LocalDate endDate) {
        return List.of();
    }
}
