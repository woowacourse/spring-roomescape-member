package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationAddRequest;
import roomescape.repository.ReservationDao;
import roomescape.repository.ReservationTimeDao;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;

    public ReservationService(ReservationDao reservationDao, ReservationTimeDao reservationTimeDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
    }

    public List<Reservation> findAllReservation() {
        return reservationDao.findAll();
    }

    public Reservation addReservation(ReservationAddRequest reservationAddRequest) {
        if (reservationDao.existByDateAndTimeId(reservationAddRequest.getDate(), reservationAddRequest.getTimeId())) {
            throw new IllegalArgumentException("예약 날짜와 예약시간이 겹치는 예약은 할 수 없습니다.");
        }
        ReservationTime reservationTime = getReservationTime(reservationAddRequest.getTimeId());
        Reservation reservationRequest = reservationAddRequest.toEntity(reservationTime);
        return reservationDao.insert(reservationRequest);
    }

    private ReservationTime getReservationTime(Long reservationTimeId) {
        return reservationTimeDao.findById(reservationTimeId)
                .orElseThrow(() -> new IllegalArgumentException("존재 하지 않는 예약시각으로 예약할 수 없습니다."));
    }

    public void removeReservation(Long id) {
        if (reservationDao.findById(id).isEmpty()) {
            throw new IllegalArgumentException("해당 id를 가진 예약이 존재하지 않습니다.");
        }
        reservationDao.deleteById(id);
    }
}
