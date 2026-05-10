package roomescape.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import roomescape.dto.TimeRequest;
import roomescape.dto.TimeResponse;
import roomescape.model.ReservationTime;
import roomescape.repository.TimeRepository;

@Service
public class TimeService {

    private final TimeRepository timeRepository;

    public TimeService(TimeRepository timeRepository) {
        this.timeRepository = timeRepository;
    }

    public List<TimeResponse> readAll() {
        List<ReservationTime> times = timeRepository.findAll();
        return times.stream()
                .map(TimeResponse::from)
                .collect(Collectors.toList());
    }

    public List<TimeResponse> readAllByThemeIdAndDate(Long themeId, String date) {
        List<ReservationTime> times = timeRepository.findAllByThemeIdAndDate(themeId, date);
        return times.stream()
                .map(TimeResponse::from)
                .collect(Collectors.toList());
    }

    public void removeById(Long id) {
        int deleteCnt = timeRepository.deleteById(id);
        if (deleteCnt == 0) {
            throw new IllegalArgumentException("존재하지 않는 시간의 ID 입니다.");
        }
    }

    public TimeResponse register(TimeRequest timeRequest) {
        if (timeRepository.existsByStartAt(timeRequest.startAt())) {
            throw new IllegalArgumentException("이미 존재하는 시간입니다.");
        }

        ReservationTime reservationTime = timeRepository.save(timeRequest.startAt());
        return TimeResponse.from(reservationTime);
    }
}
