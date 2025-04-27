package roomescape.reservation.application;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.reservation.infrastructure.ReservationDao;
import roomescape.reservation.infrastructure.ReservationTimeDao;
import roomescape.reservation.presentation.dto.ReservationRequest;
import roomescape.reservation.presentation.dto.ReservationResponse;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;

@Service
public class ReservationService {
    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;

    public ReservationService(final ReservationDao reservationDao, final ReservationTimeDao reservationTimeDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
    }

    public ReservationResponse createReservation(final ReservationRequest reservationRequest) {
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

    public void deleteReservation(final Long id) {
        reservationDao.delete(id);
    }
}
