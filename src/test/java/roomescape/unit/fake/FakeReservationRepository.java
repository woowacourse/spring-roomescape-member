package roomescape.unit.fake;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.domain.repository.ReservationRepository;
import roomescape.dto.request.ReservationCondition;

public class FakeReservationRepository implements ReservationRepository {

    private final List<Reservation> fakeReservations = new ArrayList<>();
    private final AtomicLong index = new AtomicLong(1);

    public FakeReservationRepository(Reservation... reservations) {
        Arrays.stream(reservations).forEach(reservation -> fakeReservations.add(reservation));
    }

    @Override
    public List<Reservation> findAll() {
        return new ArrayList<>(fakeReservations);
    }

    @Override
    public Reservation create(Reservation reservation) {
        Reservation reservationWithId = Reservation.of(index.getAndIncrement(), reservation.getMember(),
                reservation.getDate(), reservation.getReservationTime(), reservation.getTheme());
        fakeReservations.add(reservationWithId);
        return reservationWithId;
    }

    @Override
    public void delete(Long id) {
        fakeReservations.removeIf(reservation -> reservation.getId().equals(id));
    }

    @Override
    public List<Reservation> findByTimeId(Long id) {
        return fakeReservations.stream()
                .filter(reservation -> reservation.getReservationTime().getId().equals(id))
                .toList();
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        return fakeReservations.stream()
                .filter(reservation -> reservation.getId().equals(id))
                .findFirst();
    }

    @Override
    public Optional<Reservation> findByDateTimeAndTheme(LocalDate date, ReservationTime time, Theme theme) {
        return fakeReservations.stream()
                .filter(reservation -> reservation.getDate().equals(date))
                .filter(reservation -> reservation.getReservationTime().getId().equals(time.getId()))
                .filter(reservation -> reservation.getTheme().getId().equals(theme.getId()))
                .findFirst();
    }

    @Override
    public List<Reservation> findByCondition(ReservationCondition cond) {
        List<Reservation> filteredReservations = new ArrayList<>(fakeReservations);
        if (cond.memberId() != null) {
            filteredReservations = fakeReservations.stream()
                    .filter(reservation -> cond.memberId().equals(reservation.getMember().getId()))
                    .toList();
        }
        if (cond.themeId() != null) {
            filteredReservations = fakeReservations.stream()
                    .filter(reservation -> cond.themeId().equals(reservation.getTheme().getId()))
                    .toList();
        }
        if (cond.dateFrom() != null && cond.dateTo() != null) {
            filteredReservations = fakeReservations.stream()
                    .filter(reservation -> reservation.getDate().isAfter(cond.dateFrom().minusDays(1)))
                    .filter(reservation -> reservation.getDate().isBefore(cond.dateTo().plusDays(1)))
                    .toList();
        }
        return filteredReservations;
    }
}
