package roomescape.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import roomescape.domain.Theme;
import roomescape.domain.ThemeSlot;
import roomescape.domain.Time;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ThemeRepository;
import roomescape.repository.ThemeSlotRepository;
import roomescape.repository.TimeRepository;

@Service
public class TimeService {

    private final TimeRepository timeRepository;
    private final ReservationRepository reservationRepository;
    private final ThemeSlotRepository themeSlotRepository;
    private final ThemeRepository themeRepository;

    public TimeService(
            TimeRepository timeRepository,
            ReservationRepository reservationRepository,
            ThemeSlotRepository themeSlotRepository,
            ThemeRepository themeRepository
    ) {
        this.timeRepository = timeRepository;
        this.reservationRepository = reservationRepository;
        this.themeSlotRepository = themeSlotRepository;
        this.themeRepository = themeRepository;
    }

    public List<Time> allTimes() {
        return timeRepository.findAll();
    }

    public Time saveTime(LocalTime startAt) {
        Time time = new Time(startAt);
        return timeRepository.save(time);
    }

    public void removeTime(long timeId) {
        getTimeOrElseThrow(timeId);
        timeRepository.deleteById(timeId);
    }

    public Time findTime(long timeId) {
        return getTimeOrElseThrow(timeId);
    }

    public List<ThemeSlot> findThemeSlotBy(long themeId, LocalDate date) {
        boolean isExist = themeSlotRepository.isExistBy(themeId, date);
        // 존재하면 해당 테마, 해당 날짜 데이터 조회후 반환
        if (isExist) {
            return themeSlotRepository.findByThemeIdAndDate(themeId, date);
        }

        // 존재하지 않으면 생성, 저장 후 반환
        Theme theme = getThemeOrElseThrow(themeId);
        List<Time> times = timeRepository.findAll();
        List<ThemeSlot> themeSlots = new ArrayList<>();
        times.forEach(time -> themeSlots.add(new ThemeSlot(theme, date, time, false)));
        return themeSlotRepository.saveAll(themeSlots);
    }

    @NonNull
    private Theme getThemeOrElseThrow(long themeId) {
        return themeRepository.findById(themeId)
                .orElseThrow(() -> new IllegalArgumentException("테마가 존재하지 않습니다."));
    }

    @NonNull
    private Time getTimeOrElseThrow(long timeId) {
        return timeRepository.findById(timeId)
                .orElseThrow(() -> new IllegalArgumentException("타임 id가 존재하지 않습니다."));
    }
}
