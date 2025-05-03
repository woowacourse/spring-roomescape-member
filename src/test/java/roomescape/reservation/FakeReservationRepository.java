package roomescape.reservation;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.dao.EmptyResultDataAccessException;
import roomescape.reservationtime.ReservationTime;
import roomescape.theme.Theme;

public class FakeReservationRepository implements ReservationRepository {

    private Long NEXT_ID = 1L;
    private final List<Reservation> reservations = new ArrayList<>();
    private final List<Long> invokeDeleteId = new ArrayList<>();

    @Override
    public Long save(final Reservation reservation, final Long timeId, final Long themeId) {
        final Reservation writedReservation = new Reservation(
                NEXT_ID++,
                reservation.getName(),
                reservation.getDate(),
                generateReservationTimeDummy(timeId),
                generateReservationThemeDummy(themeId)
        );
        reservations.add(writedReservation);
        return writedReservation.getId();
    }

    @Override
    public List<Reservation> findAll() {
        return new ArrayList<>(reservations);
    }

    @Override
    public Reservation findById(final Long id) {
        return reservations.stream()
                .filter(reservation -> Objects.equals(reservation.getId(), id))
                .findAny()
                .orElseThrow(() -> new EmptyResultDataAccessException(1));
    }

    @Override
    public void delete(final Long id) {
        reservations.stream()
                .filter(reservation -> Objects.equals(reservation.getId(), id))
                .findAny()
                .ifPresent(reservation -> reservations.remove(reservation));
        invokeDeleteId.add(id);
    }

    @Override
    public Boolean existsById(final Long id) {
        return reservations.stream()
                .anyMatch(reservation -> Objects.equals(reservation.getId(), id));
    }

    @Override
    public Boolean existsByReservationTime(final Long reservationTimeId) {
        return reservations.stream()
                .map(reservation -> reservation.getReservationTime())
                .anyMatch(reservationTime -> Objects.equals(reservationTime.getId(), reservationTimeId));
    }

    @Override
    public Boolean existsByTheme(final Long themeId) {
        return reservations.stream()
                .map(reservation -> reservation.getTheme())
                .anyMatch(theme -> Objects.equals(theme.getId(), themeId));
    }

    @Override
    public Boolean existsByReservationTimeIdAndDate(final Long reservationTimeId, final LocalDate date) {
        return reservations.stream()
                .filter(reservation -> Objects.equals(reservation.getDate(), date))
                .map(reservation -> reservation.getReservationTime())
                .anyMatch(reservationTime -> Objects.equals(reservationTime.getId(), reservationTimeId));
    }

    public boolean isInvokeDeleteById(final Long id) {
        return invokeDeleteId.stream()
                .anyMatch(value -> Objects.equals(value, id));
    }

    public void clear() {
        NEXT_ID = 1L;
        reservations.clear();
        invokeDeleteId.clear();
    }

    private ReservationTime generateReservationTimeDummy(final Long id) {
        return new ReservationTime(
                id,
                LocalTime.of(12, 40)
        );
    }

    private Theme generateReservationThemeDummy(final Long id) {
        return new Theme(
                id,
                "",
                "",
                ""
        );
    }
}
