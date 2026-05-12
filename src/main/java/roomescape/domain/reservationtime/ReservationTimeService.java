package roomescape.domain.reservationtime;

import java.util.List;

import org.springframework.stereotype.Service;
import roomescape.exception.ErrorCode;
import roomescape.exception.RoomescapeException;
import roomescape.domain.reservationtime.dto.TimeRequest;
import roomescape.domain.reservationtime.dto.TimeResponse;

@Service
public class ReservationTimeService {

    private final ReservationTimeRepository timeRepository;

    public ReservationTimeService(ReservationTimeRepository timeRepository) {
        this.timeRepository = timeRepository;
    }

    public TimeResponse createTime(TimeRequest request) {
        if (timeRepository.existsByStartAt(request.startAt())) {
            throw new RoomescapeException(ErrorCode.DUPLICATE_TIME);
        }
        ReservationTime time = ReservationTime.of(
                request.startAt(),
                request.finishAt()
        );
        ReservationTime saved = timeRepository.save(time);
        return TimeResponse.of(saved);
    }

    public List<TimeResponse> getAllTimes() {
        List<ReservationTime> times = timeRepository.findAll();

        return times.stream()
            .map(TimeResponse::of)
            .toList();
    }

    public void deleteById(Long id) {
        validateReservationTimeId(id);
        timeRepository.deleteById(id);
    }

    private void validateReservationTimeId(Long id) {
        boolean isValidId = timeRepository.existsById(id);
        if (!isValidId) {
            throw new RoomescapeException(ErrorCode.TIME_ID_NOT_FOUND);
        }
    }
}
