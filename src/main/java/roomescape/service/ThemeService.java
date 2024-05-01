package roomescape.service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Theme;
import roomescape.dto.ThemeRequest;
import roomescape.dto.ThemeResponse;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ThemeRepository;

@Service
@Transactional(readOnly = true)
public class ThemeService {

    private final ReservationRepository reservationRepository;
    private final ThemeRepository themeRepository;
    private final Clock clock;

    public ThemeService(ReservationRepository reservationRepository,
                        ThemeRepository themeRepository, Clock clock) {
        this.reservationRepository = reservationRepository;
        this.themeRepository = themeRepository;
        this.clock = clock;
    }

    public List<ThemeResponse> getAllThemes() {
        List<Theme> themes = themeRepository.findAll();

        return themes.stream()
                .map(ThemeResponse::from)
                .toList();
    }

    @Transactional
    public ThemeResponse addTheme(ThemeRequest themeRequest) {
        Theme theme = themeRequest.toTheme();

        if (themeRepository.existsByName(theme.getName())) {
            throw new IllegalArgumentException("해당 이름의 테마는 이미 존재합니다.");
        }

        Theme savedTheme = themeRepository.save(theme);

        return ThemeResponse.from(savedTheme);
    }

    @Transactional
    public void deleteThemeById(Long id) {
        if (reservationRepository.existsByThemeId(id)) {
            throw new IllegalArgumentException("해당 테마를 사용하는 예약이 존재합니다.");
        }

        themeRepository.deleteById(id);
    }

    public List<ThemeResponse> getPopularThemes() {
        LocalDateTime now = LocalDateTime.now(clock);
        List<Theme> themes = themeRepository.findTop10ThemesLastWeek(now);

        return themes.stream()
                .map(ThemeResponse::from)
                .toList();
    }
}
