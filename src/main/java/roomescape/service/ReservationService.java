package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationResponse;
import roomescape.repository.ReservationDao;
import roomescape.repository.ReservationTimeDao;

@Service
public class ReservationService {
    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;

    public ReservationService(
            final ReservationDao reservationDao,
            final ReservationTimeDao reservationTimeDao
    ) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
    }

    public ReservationResponse save(final ReservationRequest reservationRequest) {
        ReservationTime reservationTime = reservationTimeDao.findById(reservationRequest.timeId())
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 잘못된 예약 가능 시간 번호를 입력하였습니다."));
        Reservation reservation = reservationRequest.toEntity(reservationTime);
        return ReservationResponse.from(reservationDao.save(reservation));
    }

    public List<ReservationResponse> findAll() {
        List<ReservationResponse> reservations = getAll();
        if (reservations.isEmpty()) {
            throw new IllegalStateException("[ERROR] 방탈출 예약 내역이 없습니다.");
        }
        return reservations;
    }

    public List<ReservationResponse> getAll() {
        return reservationDao.getAll()
                .stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public void deleteById(final long id) {
        reservationDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 삭제할 예약 데이터가 없습니다."));
        reservationDao.delete(id);
    }
}
