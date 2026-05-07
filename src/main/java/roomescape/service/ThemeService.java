package roomescape.service;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.theme.CreateThemeRequest;
import roomescape.dto.theme.PopularThemeResponse;
import roomescape.dto.theme.ThemeReservationTimeResponse;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

@Service
public class ThemeService {

    private static final Integer POPULAR_THEME_PERIOD_DAYS = 7;

    private final ThemeRepository themeRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final Clock clock;

    public ThemeService(ThemeRepository themeRepository, ReservationRepository reservationRepository,
                        ReservationTimeRepository reservationTimeRepository, Clock clock) {
        this.themeRepository = themeRepository;
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.clock = clock;
    }

    public List<Theme> getThemes() {
        return themeRepository.findAll();
    }

    @Transactional
    public Theme createTheme(CreateThemeRequest request) {
        Theme theme = new Theme(null, request.name(), request.description(), request.thumbnailImageUrl());
        Long newThemeId = themeRepository.save(theme);
        return themeRepository.findById(newThemeId);
    }

    public void deleteTheme(Long id) {
        themeRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<ThemeReservationTimeResponse> getThemeTimes(Long themeId, LocalDate date) {
        List<Long> reservedTimeIds = reservationRepository.findTimeIdsByThemeIdAndDate(themeId, date);
        List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();

        return reservationTimes.stream()
                .map(time -> new ThemeReservationTimeResponse(
                        time.getId(),
                        time.getStartAt().toString(),
                        reservedTimeIds.contains(time.getId())
                ))
                .toList();
    }

    public List<PopularThemeResponse> getPopularThemes(Integer limit) {
        LocalDate today = LocalDate.now(clock);
        LocalDate to = today.minusDays(1);
        LocalDate from = today.minusDays(POPULAR_THEME_PERIOD_DAYS);

        return themeRepository.findPopularThemes(from, to, limit);
    }
}
