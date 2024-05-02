package roomescape.console.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.core.domain.Reservation;
import roomescape.core.dto.BookingTimeResponseDto;
import roomescape.core.repository.ReservationRepository;

public class ReservationConsoleRepository implements ReservationRepository {
    private final List<Reservation> reservations;
    private final AtomicLong id;

    public ReservationConsoleRepository() {
        reservations = new ArrayList<>();
        id = new AtomicLong(1);
    }

    @Override
    public Long save(final Reservation reservation) {
        final Reservation persistReservation = new Reservation(
                id.getAndIncrement(),
                reservation.getName(),
                reservation.getDateString(),
                reservation.getReservationTime(),
                reservation.getTheme());
        reservations.add(persistReservation);
        return persistReservation.getId();
    }

    @Override
    public List<Reservation> findAll() {
        return Collections.unmodifiableList(reservations);
    }

    @Override
    public List<BookingTimeResponseDto> findAllByDateAndThemeId(String date,
        long themeId) {
        return List.of();
    }

    @Override
    public Integer countByTimeId(final long timeId) {
        return null;
    }

    @Override
    public Integer countByThemeId(final long themeId) {
        return null;
    }

    @Override
    public Integer countByDateAndTimeIdAndThemeId(final String date, final long timeId, final long themeId) {
        return null;
    }

    @Override
    public void deleteById(final long id) {
        reservations.removeIf(reservation -> reservation.getId() == id);
    }
}
