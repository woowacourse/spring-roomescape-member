package roomescape.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import roomescape.model.Reservation;
import roomescape.model.ReservationTime;
import roomescape.model.Theme;
import roomescape.repository.ReservationDao;

class FakeReservationDao implements ReservationDao {

    private final List<Reservation> reservations = new ArrayList<>(List.of(
            new Reservation(1L, "브라운", LocalDate.of(2030, 8, 5),
                    new ReservationTime(2L, LocalTime.of(11, 0)),
                    new Theme(1L, "에버", "공포", "공포.jpg")),
            new Reservation(2L, "리사", LocalDate.of(2030, 8, 1),
                    new ReservationTime(2L, LocalTime.of(11, 0)),
                    new Theme(2L, "배키", "스릴러", "스릴러.jpg"))));

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
    public long countReservationByDateAndTimeId(LocalDate date, long timeId) {
        return reservations.stream()
                .filter(reservation -> reservation.getDate().isEqual(date)
                        && reservation.getTime().getId() == timeId)
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
