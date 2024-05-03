package roomescape.service;

import roomescape.model.Reservation;
import roomescape.model.ReservationTime;
import roomescape.model.Theme;
import roomescape.repository.ReservationDao;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

class FakeReservationDao implements ReservationDao {

    private List<Reservation> reservations = new ArrayList<>(List.of(
            new Reservation(1, "브라운", LocalDate.of(2030, 8, 5),
                    new ReservationTime(2, LocalTime.of(11, 0)),
                    new Theme(1, "에버", "공포", "공포.jpg")),
            new Reservation(1, "리사", LocalDate.of(2030, 8, 1),
                    new ReservationTime(2, LocalTime.of(11, 0)),
                    new Theme(2, "배키", "스릴러", "스릴러.jpg"))));

    @Override
    public List<Reservation> findAllReservations() {
        return reservations;
    }

    @Override
    public Reservation saveReservation(Reservation reservation) {
        reservations.add(reservation);
        return reservation;
    }

    @Override
    public void deleteReservationById(long id) {
        Reservation foundReservation = reservations.stream()
                .filter(reservation -> reservation.getId() == id)
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("아이디가 존재하지 않습니다."));
        reservations.remove(foundReservation);
    }

    @Override
    public Long countReservationById(long id) {
        return reservations.stream()
                .filter(reservation -> reservation.getId() == id)
                .count();
    }

    @Override
    public Long countReservationByTimeId(long timeId) {
        return reservations.stream()
                .filter(reservation -> reservation.getTime().getId() == timeId)
                .count();
    }

    @Override
    public Long countReservationByDateAndTimeId(LocalDate date, long timeId) {
        return reservations.stream()
                .filter(reservation -> reservation.getDate().isEqual(date)
                        && reservation.getTime().getId() == timeId)
                .count();
    }

    @Override
    public List<ReservationTime> findReservationTimeByDateAndThemeId(LocalDate date, long themeId) {
        return reservations.stream()
                .filter(reservation -> reservation.getDate().equals(date) && reservation.getTheme().getThemeId() == themeId)
                .map(reservation -> new ReservationTime(reservation.getTime().getId(), reservation.getTime().getStartAt()))
                .toList();
    }
}