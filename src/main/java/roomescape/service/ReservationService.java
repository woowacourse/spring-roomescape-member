package roomescape.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.controller.dto.ReservationRequest;
import roomescape.controller.dto.ReservationResponse;
import roomescape.repository.ReservationDao;
import roomescape.repository.ReservationTimeDao;
import roomescape.service.reservation.Reservation;
import roomescape.service.reservation.ReservationTime;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;

    public ReservationService(final ReservationDao reservationDao, final ReservationTimeDao reservationTimeDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
    }

    public ReservationResponse createReservation(final ReservationRequest reservationRequest) {
        if (reservationDao.isExistsByDateAndTimeId(reservationRequest.date(), reservationRequest.timeId())) {
            throw new IllegalArgumentException("해당 시간에 이미 예약이 존재합니다.");
        }
        if (reservationTimeDao.isNotExistsById(reservationRequest.timeId())) {
            throw new IllegalArgumentException("예약 시간이 존재하지 않습니다.");
        }
        final ReservationTime reservationTime = reservationTimeDao.findById(reservationRequest.timeId());
        final Reservation reservation = reservationRequest.convertToReservation(reservationTime);
        if (reservation.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("지나간 날짜와 시간은 예약 불가합니다.");
        }
        final Reservation savedReservation = reservationDao.save(reservation);
        return new ReservationResponse(savedReservation);
    }

    public List<ReservationResponse> getReservations() {
        return reservationDao.findAll().stream()
                .map(ReservationResponse::new)
                .toList();
    }

    public void cancelReservationById(final long id) {
        reservationDao.deleteById(id);
    }
}
