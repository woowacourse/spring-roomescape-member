package roomescape.time.service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.Time;
import roomescape.time.dto.AvailableTimeRequest;
import roomescape.time.dto.AvailableTimeResponse;
import roomescape.time.dto.TimeRequest;
import roomescape.time.dto.TimeResponse;
import roomescape.theme.repository.ThemeRepository;
import roomescape.time.repository.TimeRepository;

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
        Time newTime = new Time(null, request.startAt());

        return TimeResponse.from(timeRepository.add(newTime));
    }

    public List<TimeResponse> findAll() {
        return timeRepository.findAll().stream()
                .map(TimeResponse::from)
                .toList();
    }

    public List<AvailableTimeResponse> findByDateAndTheme(AvailableTimeRequest request) {
        ensureThemeExists(request.themeId());

        List<Long> occupiedTimeIds = reservationRepository.findTimeIdByDateAndThemeId(request.date(), request.themeId());;

        return timeRepository.findAll().stream()
                .map(time -> new AvailableTimeResponse(
                        time.id(),
                        time.startAt(),
                        occupiedTimeIds.contains(time.id())))
                .toList();
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
        if(timeRepository.existsByStartAt(startAt)) {
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
