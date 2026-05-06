package roomescape.reservationtime.service;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.dto.ReservationTimeCreateRequest;
import roomescape.reservationtime.dto.ReservationTimeResponse;
import roomescape.reservationtime.exception.ReservationTimeException;
import roomescape.reservationtime.repository.ReservationTimeRepository;

@RequiredArgsConstructor
@Service
public class ReservationTimeService {

    private final ReservationTimeRepository timeRepository;

    public ReservationTime findById(Long timeId) {
        return timeRepository.findById(timeId)
                .orElseThrow(() -> new ReservationTimeException("[ERROR] 존재하지 않는 시간 입니다."));
    }

    public List<ReservationTimeResponse> findAll() {
        List<ReservationTime> times = timeRepository.findAll();

        return times.stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }

    public List<ReservationTimeResponse> findAvailableTimes(Long themeId, LocalDate date) {
        List<ReservationTime> times = timeRepository.findByThemeAndDate(themeId, date);

        return times.stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }

    public ReservationTimeResponse save(ReservationTimeCreateRequest request) {
        ReservationTime time = request.toEntity();

        ReservationTime savedTime = timeRepository.save(time);

        return ReservationTimeResponse.from(savedTime);
    }

    public int delete(Long id) {
        return timeRepository.delete(id);
    }
}
