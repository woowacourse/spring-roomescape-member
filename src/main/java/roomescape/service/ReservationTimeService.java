package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.ReservationTime;
import roomescape.exception.ErrorCode;
import roomescape.exception.RoomescapeException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;

@Service
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationRepository reservationRepository;

    public ReservationTimeService(
            ReservationTimeRepository reservationTimeRepository,
            ReservationRepository reservationRepository
    ) {
        this.reservationTimeRepository = reservationTimeRepository;
        this.reservationRepository = reservationRepository;
    }

    public ReservationTime saveReservationTime(ReservationTime reservationTime) {
        return reservationTimeRepository.addTime(reservationTime);
    }

    public List<ReservationTime> findAllReservationTimes() {
        return reservationTimeRepository.findAllReservationTimes();
    }

    public void deleteReservationTime(Long id) {
        validateReservationTimeExists(id);
        validateReservationTimeNotInUse(id);

        reservationTimeRepository.deleteTime(id);
    }

    private void validateReservationTimeExists(Long id) {
        if (!reservationTimeRepository.existsById(id)) {
            throw new RoomescapeException(ErrorCode.RESERVATION_TIME_NOT_FOUND);
        }
    }

    private void validateReservationTimeNotInUse(Long id) {
        if (reservationRepository.existsByTimeId(id)) {
            throw new RoomescapeException(ErrorCode.RESERVATION_TIME_IN_USE);
        }
    }
}
