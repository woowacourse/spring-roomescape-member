package roomescape.reservation.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.reservation.dao.ReservationDao;
import roomescape.reservation.dao.ReservationTimeDao;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservation.entity.Reservation;
import roomescape.reservation.entity.ReservationTime;

@Service
public class ReservationService {
    private ReservationDao reservationDao;
    private ReservationTimeDao reservationTimeDao;

    public ReservationService(ReservationDao reservationDao, ReservationTimeDao reservationTimeDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
    }

    public ReservationResponse createReservation(ReservationRequest reservationRequest) {
        ReservationTime reservationTime = reservationTimeDao.findById(reservationRequest.getTimeId())
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 예약 시간 정보를 찾을 수 없습니다."));

        Reservation reservation = new Reservation(
                null,
                reservationRequest.getName(),
                reservationRequest.getDate(),
                reservationTime
        );

        return new ReservationResponse(reservationDao.insert(reservation));
    }

    public List<ReservationResponse> getReservations() {
        return reservationDao.findAllReservations().stream()
                .map(ReservationResponse::new)
                .toList();
    }

    public void deleteReservation(Long id) {
        reservationDao.delete(id);
    }
}
