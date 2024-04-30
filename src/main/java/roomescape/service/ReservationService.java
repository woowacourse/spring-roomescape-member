package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDAO;
import roomescape.dao.ReservationTimeDAO;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationRequest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class ReservationService {
    private final ReservationDAO reservationDAO;
    private final ReservationTimeDAO reservationTimeDAO;

    public ReservationService(final ReservationDAO reservationDAO, final ReservationTimeDAO reservationTimeDAO) {
        this.reservationDAO = reservationDAO;
        this.reservationTimeDAO = reservationTimeDAO;
    }

    public Reservation save(final ReservationRequest reservationRequest) {
        validateReservation(reservationRequest);
        final ReservationTime reservationTime = reservationTimeDAO.findById(reservationRequest.timeId());
        final Reservation reservation = reservationRequest.toEntity(reservationTime);

        return reservationDAO.insert(reservation);
    }

    private void validateReservation(ReservationRequest reservationRequest) {
        validateDuplicated(reservationRequest);
        validatePast(reservationRequest);
    }

    private void validateDuplicated(ReservationRequest reservationRequest) {
        LocalTime reservationTime = findRequestTime(reservationRequest);
        List<Reservation> reservations = reservationDAO.selectAll();

        if (reservations.stream()
                .anyMatch(reservation -> reservation.getDate().equals(reservationRequest.date()) &&
                        reservation.getTime().isMatch(reservationTime))) {
            throw new IllegalArgumentException("예약 날짜와 예약 시간이 중복될 수 없습니다.");
        }
    }

    private void validatePast(ReservationRequest reservationRequest) {
        LocalDate date = reservationRequest.date();
        LocalTime reservationTime = findRequestTime(reservationRequest);

        LocalDate today = LocalDate.now();
        if (date.isBefore(today)) {
            throw new IllegalArgumentException("지나간 날짜에 예약을 등록할 수 없습니다.");
        }
        if (date.isEqual(today) && reservationTime.isBefore(LocalTime.now())) {
            throw new IllegalArgumentException("지나간 시간에 예약을 등록할 수 없습니다.");
        }
    }

    private LocalTime findRequestTime(ReservationRequest reservationRequest) {
        Long timeId = reservationRequest.timeId();
        ReservationTime time = reservationTimeDAO.findById(timeId);
        return time.getStartAt();
    }

    public List<Reservation> findAll() {
        return reservationDAO.selectAll();
    }

    public void delete(final long id) {
        reservationDAO.deleteById(id);
    }
}
