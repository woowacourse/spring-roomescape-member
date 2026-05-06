package roomescape.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.dao.EmptyResultDataAccessException;
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

    public List<TimeResponse> readTimeAll() {
        List<ReservationTime> times = timeRepository.findAll();
        return times.stream()
                .map(TimeResponse::from)
                .collect(Collectors.toList());
    }

    public List<TimeResponse> readTimeAllByThemeIdAndDate(Long themeId, String date) {
        List<ReservationTime> times = timeRepository.findAllByThemeIdAndDate(themeId, date);
        return times.stream()
                .map(TimeResponse::from)
                .collect(Collectors.toList());
    }

    public void removeTime(Long id) {
        try {
            timeRepository.selectById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalArgumentException("삭제하고자 하는 시간 ID가 없습니다.");
        }
        timeRepository.removeById(id);
    }

    public TimeResponse registerTime(TimeRequest timeRequest) {
        if (timeRepository.existsByStartAt(timeRequest.startAt()) > 0) {
            throw new IllegalArgumentException("이미 존재하는 시간입니다.");
        }
        
        ReservationTime reservationTime = timeRepository.saveTime(timeRequest.startAt());
        return TimeResponse.from(reservationTime);
    }

    public TimeResponse findById(Long id) {
        ReservationTime reservationTime = timeRepository.selectById(id);
        return TimeResponse.from(reservationTime);
    }
}
