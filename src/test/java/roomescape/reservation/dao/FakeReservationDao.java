package roomescape.reservation.dao;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.repository.ReservationRepository;
import roomescape.reservation.domain.repository.ReservationTimeRepository;
import roomescape.reservation.domain.repository.ThemeRepository;

public class FakeReservationDao implements ReservationRepository {
    private final Map<Long, Reservation> reservations = new HashMap<>();
    private final Map<Long, Long> reservationList = new HashMap<>();
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public FakeReservationDao(ReservationTimeRepository reservationTimeRepository, ThemeRepository themeRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    @Override
    public Reservation save(final Reservation reservation) {
        long newId = reservations.size() + 1;
        Reservation savedReservation = new Reservation(
                newId,
                reservation.getDate(),
                reservation.getTime(),
                reservation.getTheme()
        );
        reservations.put(newId, savedReservation);
        return savedReservation;
    }

    @Override
    public List<Reservation> findAll() {
        return reservations.values()
                .stream()
                .toList();
    }

    @Override
    public boolean deleteById(final long reservationId) {
        if (!reservations.containsKey(reservationId)) {
            return false;
        }
        reservations.remove(reservationId);
        return true;
    }

    @Override
    public boolean existByTimeId(long timeId) {
        return reservations.values().stream()
                .anyMatch(reservation -> reservation.getTime().getId() == timeId);
    }


    @Override
    public boolean existBy(LocalDate date, long timeId, long themeId) {
        return reservations.values().stream()
                .anyMatch(reservation -> reservation.getDate().equals(date) &&
                        reservation.getTime().getId() == timeId &&
                        reservation.getTheme().getId() == themeId);
    }


    @Override
    public void saveReservationList(long reservationId, long memberId) {
        reservationList.put(reservationId, memberId);
    }

    @Override
    public long findMemberIdByReservationId(long reservationId) {
        return reservationList.get(reservationId);
    }
}
