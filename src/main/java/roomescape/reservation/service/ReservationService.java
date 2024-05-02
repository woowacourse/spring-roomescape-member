package roomescape.reservation.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.reservation.dao.ReservationDao;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservation.dto.ReservationTimeAvailabilityResponse;
import roomescape.time.dao.TimeDao;
import roomescape.time.domain.Time;

@Service
public class ReservationService {
    private final ReservationDao reservationDao;
    private final TimeDao timeDao;

    public ReservationService(ReservationDao reservationDao, TimeDao timeDao) {
        this.reservationDao = reservationDao;
        this.timeDao = timeDao;
    }

    public ReservationResponse addReservation(ReservationRequest reservationRequest) {
        Reservation reservation = reservationRequest.fromRequest();
        Time time = timeDao.findById(reservation.getReservationTime()
                .getId());
        reservation.setTime(time);
        Reservation savedReservation = reservationDao.save(reservation);
        return ReservationResponse.fromReservation(savedReservation);
    }

    public List<ReservationResponse> findReservations() {
        List<Reservation> reservations = reservationDao.findAllReservationOrderByDateAndTimeStartAt();

        return reservations.stream()
                .map(ReservationResponse::fromReservation)
                .toList();
    }

    public List<ReservationTimeAvailabilityResponse> findTimeAvailability(long themeId, LocalDate date) {
        List<Time> allTimes = timeDao.findAllReservationTimesInOrder();
        List<Reservation> reservations = reservationDao.findAllByThemeIdAndDate(themeId, date);
        List<Time> bookedTimes = extractReservationTimes(reservations);

        return allTimes.stream()
                .map(time -> ReservationTimeAvailabilityResponse.fromTime(time, isTimeBooked(time, bookedTimes)))
                .toList();
    }

    private List<Time> extractReservationTimes(List<Reservation> reservations) {
        return reservations.stream()
                .map(Reservation::getReservationTime)
                .toList();
    }

    private boolean isTimeBooked(Time time, List<Time> bookedTimes) {
        return bookedTimes.contains(time);
    }

    public void removeReservations(long reservationId) {
        reservationDao.deleteById(reservationId);
    }
}
