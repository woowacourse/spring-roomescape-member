package roomescape.fixture;

import org.springframework.dao.DuplicateKeyException;
import roomescape.dao.ReservationDao;
import roomescape.dao.row.ReservationRow;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static roomescape.fixture.FakeDatabase.generateReservationId;
import static roomescape.fixture.FakeDatabase.reservations;

public class FakeReservationDao implements ReservationDao {
    @Override
    public List<ReservationRow> findAll() {
        return List.copyOf(reservations.values());
    }

    @Override
    public Optional<ReservationRow> findById(Long id) {
        return Optional.ofNullable(reservations.get(id));
    }

    @Override
    public ReservationRow create(ReservationRow reservation) {
        boolean duplicate = existsByThemeIdAndTimeIdAndDate(
                reservation.themeRow().id(),
                reservation.timeRow().id(),
                reservation.date()
        );

        if (duplicate) {
            throw new DuplicateKeyException("uk_theme_time_date");
        }

        Long id = generateReservationId();

        ReservationRow newReservation = new ReservationRow(
                id,
                reservation.name(),
                reservation.date(),
                reservation.timeRow(),
                reservation.themeRow());

        reservations.put(id, newReservation);
        return newReservation;
    }

    @Override
    public int delete(Long id) {
        ReservationRow remove = reservations.remove(id);

        if (remove == null) {
            return 0;
        }
        return 1;
    }

    @Override
    public boolean existsById(Long id) {
        return reservations.containsKey(id);
    }

    @Override
    public boolean existsByThemeIdAndTimeIdAndDate(Long themeId, Long timeId, LocalDate date) {
        return reservations.values()
                .stream()
                .anyMatch(reservation ->
                        reservation.themeRow().id().equals(themeId)
                                && reservation.timeRow().id().equals(timeId)
                                && reservation.date().equals(date));
    }

    @Override
    public boolean existsByThemeIdAndTimeIdAndDateAndIdNot(Long themeId, Long timeId, LocalDate date, Long id) {
        return reservations.values()
                .stream()
                .anyMatch(reservation ->
                        reservation.themeRow().id().equals(themeId)
                                && reservation.timeRow().id().equals(timeId)
                                && reservation.date().equals(date)
                                && !reservation.id().equals(id));
    }

    @Override
    public boolean existsByThemeId(Long themeId) {
        return reservations.values()
                .stream()
                .anyMatch(reservation -> reservation.themeRow().id().equals(themeId));
    }

    @Override
    public boolean existsByTimeId(Long timeId) {
        return reservations.values()
                .stream()
                .anyMatch(reservation -> reservation.timeRow().id().equals(timeId));
    }

    @Override
    public List<ReservationRow> findByName(String name) {
        return reservations.values().stream()
                .filter(reservation -> reservation.name().equals(name))
                .toList();
    }

    @Override
    public ReservationRow update(ReservationRow reservation) {
        boolean duplicate = existsByThemeIdAndTimeIdAndDateAndIdNot(
                reservation.themeRow().id(),
                reservation.timeRow().id(),
                reservation.date(),
                reservation.id()
        );

        if (duplicate) {
            throw new DuplicateKeyException("uk_theme_time_date");
        }

        reservations.put(reservation.id(), reservation);

        return reservation;
    }
}
