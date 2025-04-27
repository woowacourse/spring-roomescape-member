package roomescape.reservation.application;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.reservation.application.dto.CreateReservationRequest;
import roomescape.reservation.domain.ReservationDate;
import roomescape.reservation.domain.ReservationName;
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
        CreateReservationRequest createReservationRequest = new CreateReservationRequest(
                new ReservationName(reservationRequest.getName()),
                new ReservationDate(reservationRequest.getDate()),
                findReservationTime(reservationRequest.getTimeId())
        );

        return new ReservationResponse(reservationDao.insert(createReservationRequest));
    }

    private ReservationTime findReservationTime(Long timeId) {
        return reservationTimeDao.findById(timeId)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 예약 시간 정보를 찾을 수 없습니다."));
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
