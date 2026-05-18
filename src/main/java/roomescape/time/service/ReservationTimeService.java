package roomescape.time.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.global.exception.BusinessException;
import roomescape.global.exception.ErrorCode;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.time.domain.ReservationTime;
import roomescape.time.repository.ReservationTimeRepository;

import java.time.LocalTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationRepository reservationRepository;

    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository, ReservationRepository reservationRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
        this.reservationRepository = reservationRepository;
    }

    @Transactional
    public ReservationTime createTime(LocalTime startAt) {
        ReservationTime reservationTime = new ReservationTime(null, startAt);
        return reservationTimeRepository.save(reservationTime);
    }

    public List<ReservationTime> getTimes() {
        return reservationTimeRepository.findAll();
    }

    @Transactional
    public void removeTime(Long id) {
        if (reservationRepository.existsByTimeId(id)) {
            throw new BusinessException(ErrorCode.RESERVATION_TIME_IN_USE);
        }
        reservationTimeRepository.remove(id);
    }
}
