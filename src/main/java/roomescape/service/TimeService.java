package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
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

    public List<TimeResponse> readAll() {
        List<ReservationTime> times = timeRepository.findAll();
        return times.stream()
                .map(TimeResponse::from)
                .collect(Collectors.toList());
    }

    public List<TimeResponse> readAllByThemeIdAndDate(Long themeId, LocalDate date) {
        List<ReservationTime> times = timeRepository.findAllByThemeIdAndDate(themeId, date);
        return times.stream()
                .map(TimeResponse::from)
                .collect(Collectors.toList());
    }

    public void removeById(Long id) {
        timeRepository.findById(id).orElseThrow(
                () -> new RoomescapeException(ErrorCode.TIME_NOT_FOUND)
        );
        if (reservationRepository.existsByTimeId(id)) {
            throw new RoomescapeException(ErrorCode.TIME_HAS_RESERVATIONS);
        }
        timeRepository.deleteById(id);
    }

    public TimeResponse register(TimeRequest timeRequest) {
        if (timeRequest.startAt().getMinute() != 0) {
            throw new RoomescapeException(ErrorCode.TIME_NOT_ON_THE_HOUR);
        }
        if (timeRepository.existsByStartAt(timeRequest.startAt())) {
            throw new RoomescapeException(ErrorCode.TIME_DUPLICATED);
        }
        ReservationTime reservationTime = timeRepository.save(timeRequest.startAt());
        return TimeResponse.from(reservationTime);
    }
}
