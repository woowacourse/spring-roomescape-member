package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.ReservationTime;
import roomescape.exception.ErrorCode;
import roomescape.exception.RoomescapeException;
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
        reservationTimeRepository.findById(id)
                .orElseThrow(() -> new RoomescapeException(ErrorCode.RESERVATION_TIME_NOT_FOUND));

        if (reservationTimeRepository.existsByTimeId(id)) {
            throw new RoomescapeException(ErrorCode.RESERVATION_TIME_IN_USE);
        }

        reservationTimeRepository.deleteTime(id);
    }
}
