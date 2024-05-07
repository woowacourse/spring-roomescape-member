package roomescape.service.fake;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import roomescape.model.Reservation;
import roomescape.model.ReservationTime;
import roomescape.repository.dao.ReservationDao;

public class FakeReservationDao implements ReservationDao {

    private final List<Reservation> reservations = new ArrayList<>();

    @Override
    public List<Reservation> getAllReservations() {
        return reservations;
    }

    @Override
    public Reservation addReservation(Reservation reservation) {
        reservations.add(reservation);
        return reservation;
    }

    @Override
    public void deleteReservation(long id) {
        Reservation foundReservation = reservations.stream()
                .filter(reservation -> reservation.getId() == id)
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("아이디가 존재하지 않습니다."));
        reservations.remove(foundReservation);
    }

    @Override
    public long countReservationById(long id) {
        return reservations.stream()
                .filter(reservation -> reservation.getId() == id)
                .count();
    }

    @Override
    public long countReservationByTimeId(long timeId) {
        return reservations.stream()
                .filter(reservation -> reservation.getTime().getId() == timeId)
                .count();
    }

    @Override
    public long countReservationByDateAndTimeId(LocalDate date, long timeId, long themeId) {
        return reservations.stream()
                .filter(reservation -> reservation.getDate().isEqual(date)
                        && reservation.getTime().getId() == timeId
                        && reservation.getTheme().getThemeId() == themeId)
                .count();
    }

    @Override
    public List<ReservationTime> findReservationTimeByDateAndTheme(LocalDate date, long themeId) {
        return reservations.stream()
                .filter(reservation -> reservation.getDate().equals(date)
                        && reservation.getTheme().getThemeId() == themeId)
                .map(reservation -> new ReservationTime(reservation.getTime().getId(),
                        reservation.getTime().getStartAt()))
                .toList();
    }
}
