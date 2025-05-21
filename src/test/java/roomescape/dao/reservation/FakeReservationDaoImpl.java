package roomescape.dao.reservation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.common.exception.reservation.InvalidReservationException;
import roomescape.domain.reservation.Reservation;

public class FakeReservationDaoImpl implements ReservationDao {

    private final List<Reservation> reservations = new ArrayList<>();
    private final AtomicLong index = new AtomicLong(1);

    @Override
    public List<Reservation> findAllReservation() {
        return Collections.unmodifiableList(reservations);
    }

    @Override
    public List<Reservation> findByThemeIdAndMemberIDAndDateFromAndDateTo(
            final LocalDate dateFrom,
            final LocalDate dateTo,
            final Long themeId,
            final Long memberId
    ) {
        return reservations.stream()
                .filter(reservation -> !reservation.getDate().isBefore(dateFrom))
                .filter(reservation -> !reservation.getDate().isAfter(dateTo))
                .filter(reservation -> reservation.getThemeId().equals(themeId))
                .filter(reservation -> reservation.getMemberId().equals(memberId))
                .toList();
    }

    @Override
    public void saveReservation(final Reservation reservation) {
        reservation.setId(index.getAndIncrement());
        reservations.add(reservation);
    }

    @Override
    public void deleteReservation(final Long id) {
        final Reservation reservation = findById(id);
        reservations.remove(reservation);
    }

    private Reservation findById(final long id) {
        return reservations.stream()
                .filter(reservation -> reservation.getId() == id)
                .findFirst()
                .orElseThrow(() -> new InvalidReservationException("존재하지 않는 예약번호 입니다."));
    }

    @Override
    public Boolean existsReservationBy(final LocalDate date, final Long timeId, final Long themeId) {
        return reservations.stream()
                .anyMatch(reservation ->
                        reservation.getDate().equals(date)
                        && reservation.getTimeId().equals(timeId)
                        && reservation.getThemeId().equals(themeId));
    }
}
