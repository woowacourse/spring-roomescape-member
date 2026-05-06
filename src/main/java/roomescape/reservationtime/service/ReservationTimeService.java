package roomescape.reservationtime.service;

import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.repository.ReservationTimeRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.service.ThemeService;

@Service
@Transactional(readOnly = true)
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeService themeService;

    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository, ThemeService themeService) {
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeService = themeService;
    }

    @Transactional
    public ReservationTime save(final LocalTime startAt, final Long themeId) {
        Theme theme = themeService.getById(themeId);
        ReservationTime reservationTime = ReservationTime.createNew(startAt, theme);

        if (reservationTimeRepository.existsByStartAtAndThemeId(startAt, themeId)) {
            throw new IllegalArgumentException("[ERROR] 같은 시간을 추가할 수 없습니다.");
        }

        return reservationTimeRepository.save(reservationTime);
    }

    @Transactional
    public void deleteById(final long timeId) {
        reservationTimeRepository.deleteById(timeId);
    }

    public List<ReservationTime> findAllByThemeId(final long themeId) {
        return reservationTimeRepository.findAllByThemeId(themeId);
    }

    public ReservationTime getById(final long timeId) {
        return reservationTimeRepository.findById(timeId)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 찾는 시간이 없습니다"));
    }

    public List<ReservationTime> findAll() {
        return reservationTimeRepository.findAll();
    }

}
