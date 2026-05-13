package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dto.TimeAllResponse;
import roomescape.dto.TimeRequest;
import roomescape.dto.TimeResponse;
import roomescape.exception.ErrorCode;
import roomescape.exception.RoomescapeException;
import roomescape.model.ReservationTime;
import roomescape.repository.ReservationRepository;
import roomescape.repository.TimeRepository;

@Service
public class TimeService {

    private final TimeRepository timeRepository;
    private final ReservationRepository reservationRepository;

    public TimeService(TimeRepository timeRepository, ReservationRepository reservationRepository) {
        this.timeRepository = timeRepository;
        this.reservationRepository = reservationRepository;
    }

    public TimeAllResponse readAll() {
        List<ReservationTime> times = timeRepository.findAll();
        List<TimeResponse> responses = times.stream()
                .map(TimeResponse::from)
                .toList();
        return new TimeAllResponse(responses);
    }

    public TimeAllResponse readAllByThemeIdAndDate(Long themeId, String date) {
        List<ReservationTime> times = timeRepository.findAllByThemeIdAndDate(themeId, date);
        List<TimeResponse> responses = times.stream()
                .map(TimeResponse::from)
                .toList();
        return new TimeAllResponse(responses);
    }

    public void removeById(Long id) {
        if (reservationRepository.existsByTimeId(id)) {
            throw new RoomescapeException(ErrorCode.TIME_CANNOT_DELETE);
        }
        int deleteCnt = timeRepository.deleteById(id);
        if (deleteCnt == 0) {
            throw new RoomescapeException(ErrorCode.TIME_NOT_FOUND);
        }
    }

    public TimeResponse register(TimeRequest timeRequest) {
        if (timeRequest.startAt() == null) {
            throw new RoomescapeException(ErrorCode.TIME_BLANK_STARTAT);
        }
        if (timeRepository.existsByStartAt(timeRequest.startAt())) {
            throw new RoomescapeException(ErrorCode.TIME_DUPLICATE);
        }
        ReservationTime reservationTime = timeRepository.save(timeRequest.startAt());
        return TimeResponse.from(reservationTime);
    }
}
