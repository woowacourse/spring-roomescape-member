package roomescape.reservation.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.exception.ConflictException;
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
        this.reservationDao = reservationDao; this.timeDao = timeDao;
    }

    public ReservationResponse addReservation(ReservationRequest reservationRequest) {
        Reservation reservation = reservationRequest.toReservation();
        Time time = timeDao.findById(reservation.getReservationTimeId());
        validateDuplicateReservation(reservationRequest, time); reservation.setTime(time);
        Reservation savedReservation = reservationDao.save(reservation);
        return ReservationResponse.fromReservation(savedReservation);
    }

    private void validateDuplicateReservation(ReservationRequest reservationRequest, Time time) {
        List<Time> bookedTimes = getBookedTimesOfThemeAtDate(reservationRequest.themeId(), reservationRequest.date());
        if (isTimeBooked(time, bookedTimes)) {
            throw new ConflictException("해당 시간에 예약이 존재합니다.");
        }
    }

    public List<ReservationResponse> findReservations() {
        List<Reservation> reservations = reservationDao.findAllOrderByDateAndTime();

        return reservations.stream()
                .map(ReservationResponse::fromReservation)
                .toList();
    }

    public List<ReservationTimeAvailabilityResponse> findTimeAvailability(long themeId, LocalDate date) {
        List<Time> allTimes = timeDao.findAllReservationTimesInOrder();
        List<Time> bookedTimes = getBookedTimesOfThemeAtDate(themeId, date);

        return allTimes.stream()
                .map(time -> ReservationTimeAvailabilityResponse.fromTime(time, isTimeBooked(time, bookedTimes)))
                .toList();
    }

    private List<Time> getBookedTimesOfThemeAtDate(long themeId, LocalDate date) {
        List<Reservation> reservationsOfThemeInDate = reservationDao.findAllByThemeIdAndDate(themeId, date);
        return extractReservationTimes(reservationsOfThemeInDate);
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
