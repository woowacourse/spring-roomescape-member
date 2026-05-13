package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.ReservationTime;
import roomescape.repository.ReservationTimeRepository;

@Service
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public ReservationTime saveReservationTime(ReservationTime reservationTime) {
        return reservationTimeRepository.addTime(reservationTime);
    }

    public List<ReservationTime> findAllReservationTimes() {
        return reservationTimeRepository.findAllReservationTimes();
    }

    public void deleteReservationTime(Long id) {
        if (reservationTimeRepository.existsByTimeId(id)) {
            throw new IllegalArgumentException("해당 시간의 예약이 존재합니다.");
        }

        reservationTimeRepository.deleteTime(id);
    }
}
