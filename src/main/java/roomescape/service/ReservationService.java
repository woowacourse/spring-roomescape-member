package roomescape.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationRequestDto;

import java.util.List;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;

    public ReservationService(ReservationDao reservationDao, ReservationTimeDao reservationTimeDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
    }

    public List<Reservation> getAllReservations() {
        return reservationDao.findAll();
    }

    @Transactional
    public Reservation insertReservation(ReservationRequestDto reservationRequestDto) {
        Long id = reservationDao.insert(
                reservationRequestDto.name(), reservationRequestDto.date().toString(), reservationRequestDto.timeId());
        ReservationTime reservationTime = reservationTimeDao.findById(reservationRequestDto.timeId())
                .orElseThrow(() -> new IllegalArgumentException("올바르지 않은 입력입니다."));

        return new Reservation(id, reservationRequestDto.name(), reservationRequestDto.date().toString(), reservationTime);
    }

    public void deleteReservation(Long id) {
        reservationDao.deleteById(id);
    }
}
