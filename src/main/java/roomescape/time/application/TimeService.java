package roomescape.time.application;

import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.reservation.infrastructure.ReservationRepository;
import roomescape.time.domain.Time;
import roomescape.time.dto.AvailableTimeRequest;
import roomescape.time.dto.AvailableTimeResponse;
import roomescape.time.dto.TimeRequest;
import roomescape.time.dto.TimeResponse;
import roomescape.theme.infrastructure.ThemeRepository;
import roomescape.time.infrastructure.TimeRepository;

@Service
public class TimeService {

    private final TimeRepository timeRepository;
    private final ReservationRepository reservationRepository;
    private final ThemeRepository themeRepository;

    public TimeService(TimeRepository timeRepository,
                       ReservationRepository reservationRepository,
                       ThemeRepository themeRepository) {

        this.timeRepository = timeRepository;
        this.reservationRepository = reservationRepository;
        this.themeRepository = themeRepository;
    }

    public TimeResponse add(TimeRequest request) {
        validateNoDuplicateTime(request.startAt());

        Time time = Time.createBeforeSaved(request.startAt());
        return TimeResponse.from(timeRepository.add(time));
    }

    public List<TimeResponse> findAll() {
        return timeRepository.findAll().stream()
                .map(TimeResponse::from)
                .toList();
    }

    public List<AvailableTimeResponse> findByDateAndThemeId(AvailableTimeRequest request) {
        ensureThemeExists(request.themeId());

        return timeRepository.findByDateAndThemeId(request.date(), request.themeId());
    }

    private void ensureThemeExists(Long themeId) {
        themeRepository.findById(themeId)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 테마 id가 존재하지 않습니다."));
    }

    public void deleteById(Long id) {
        ensureExistsTime(id);
        validateTimeNotInReservation(id);

        timeRepository.deleteById(id);
    }

    private void validateNoDuplicateTime(LocalTime startAt) {
        if (timeRepository.existsByStartAt(startAt)) {
            throw new IllegalArgumentException("[ERROR] 이미 해당 시간이 존재합니다.");
        }
    }

    private void validateTimeNotInReservation(Long timeId) {
        if (reservationRepository.existsByTimeId(timeId)) {
            throw new IllegalArgumentException("[ERROR] 예약된 시간은 삭제할 수 없습니다.");
        }
    }

    private void ensureExistsTime(Long id) {
        timeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 해당 id의 시간이 존재하지 않습니다."));
    }
}
