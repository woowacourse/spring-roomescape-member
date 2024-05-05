package roomescape.time.service;

import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.exception.model.RoomEscapeException;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.time.domain.Time;
import roomescape.time.dto.TimeRequest;
import roomescape.time.dto.TimeResponse;
import roomescape.time.exception.TimeExceptionCode;
import roomescape.time.repository.TimeRepository;

@Service
public class TimeService {

    private final TimeRepository timeRepository;
    private final ReservationRepository reservationRepository;

    public TimeService(TimeRepository timeRepository, ReservationRepository reservationRepository) {
        this.timeRepository = timeRepository;
        this.reservationRepository = reservationRepository;
    }

    public TimeResponse addReservationTime(TimeRequest timeRequest) {
        validateDuplicateTime(timeRequest.startAt());
        Time reservationTime = new Time(timeRequest.startAt());
        Time savedReservationTime = timeRepository.save(reservationTime);

        return toResponse(savedReservationTime);
    }

    public List<TimeResponse> findReservationTimes() {
        List<Time> reservationTimes = timeRepository.findAllReservationTimesInOrder();

        return reservationTimes.stream()
                .map(this::toResponse)
                .toList();
    }

    public void removeReservationTime(long reservationTimeId) {
        validateReservationExistence(reservationTimeId);
        timeRepository.deleteById(reservationTimeId);
    }

    public TimeResponse toResponse(Time time) {
        return new TimeResponse(time.getId(), time.getStartAt());
    }

    public void validateDuplicateTime(LocalTime startAt) {
        int duplicateTimeCount = timeRepository.countByStartAt(startAt);
        if (duplicateTimeCount > 0) {
            throw new RoomEscapeException(TimeExceptionCode.DUPLICATE_TIME_EXCEPTION);
        }
    }

    public void validateReservationExistence(long timeId) {
        int reservationCount = reservationRepository.countByTimeId(timeId);
        if (reservationCount > 0) {
            throw new RoomEscapeException(TimeExceptionCode.EXIST_RESERVATION_AT_CHOOSE_TIME);
        }
    }
}
