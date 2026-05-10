package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import roomescape.dto.TimeRequest;
import roomescape.dto.TimeResponse;
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
        try {
            timeRepository.findById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalArgumentException("삭제하고자 하는 시간 ID가 없습니다.");
        }

        if (reservationRepository.existsByTimeId(id)) {
            throw new IllegalArgumentException("현재 예약이 존재하는 시간은 삭제할 수 없습니다.");
        }

        timeRepository.deleteById(id);
    }

    public TimeResponse register(TimeRequest timeRequest) {
        if (timeRepository.existsByStartAt(timeRequest.startAt())) {
            throw new IllegalArgumentException("이미 존재하는 시간입니다.");
        }

        ReservationTime reservationTime = timeRepository.save(timeRequest.startAt());
        return TimeResponse.from(reservationTime);
    }

    public TimeResponse readById(Long id) {
        ReservationTime reservationTime = timeRepository.findById(id);
        return TimeResponse.from(reservationTime);
    }
}
