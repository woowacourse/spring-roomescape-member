package roomescape.reservation.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.reservation.domain.Schedule;
import roomescape.reservation.infra.ScheduleRepository;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    public long getScheduleId(LocalDate date, long timeId, long themeId) {
        return findByDateAndTimeIdAndThemeId(date, timeId, themeId).getId();
    }

    public void validateTimeDeletable(long timeId) {
        if (scheduleRepository.existsByTimeId(timeId)) {
            throw new IllegalStateException("timeId= " + timeId + " 인 시간을 사용하는 스케줄이 있어 삭제할 수 없습니다.");
        }
    }

    public void validateThemeDeletable(long themeId) {
        if (scheduleRepository.existsByThemeId(themeId)) {
            throw new IllegalStateException("themeId= " + themeId + " 인 테마를 사용하는 스케줄이 있어 삭제할 수 없습니다.");
        }
    }

    private Schedule findByDateAndTimeIdAndThemeId(LocalDate date, long timeId, long themeId) {
        return scheduleRepository.findByDateAndTimeIdAndThemeId(date, timeId, themeId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "해당 조건을 가진 일정이 없습니다. date: " + date + "timeId: " + timeId + "themeId: " + themeId
                ));
    }
}
