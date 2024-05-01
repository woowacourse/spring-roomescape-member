package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Theme;
import roomescape.persistence.ReservationRepository;
import roomescape.persistence.ThemeRepository;
import roomescape.service.request.ThemeRequest;
import roomescape.service.response.ThemeResponse;

@Service
public class ThemeService {
    private final ThemeRepository themeRepository;
    private final ReservationRepository reservationRepository;

    public ThemeService(ThemeRepository themeRepository, ReservationRepository reservationRepository) {
        this.themeRepository = themeRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<ThemeResponse> findAll(boolean showRanking) {
        if (showRanking) {
            LocalDate startDate = LocalDate.now().minusDays(7);
            LocalDate endDate = LocalDate.now().minusDays(1);
            int limitCount = 10;
            return themeRepository.findPopularThemes(startDate, endDate, limitCount).stream()
                    .map(ThemeResponse::from)
                    .toList();
        }
        return themeRepository.findAll().stream()
                .map(ThemeResponse::from)
                .toList();
    }

    public ThemeResponse create(ThemeRequest themeRequest) {
        Theme theme = themeRequest.toDomain();
        Theme createdTheme = themeRepository.create(theme);
        return ThemeResponse.from(createdTheme);
    }

    public void deleteTheme(long id) {
        if (reservationRepository.hasByThemeId(id)) {
            throw new IllegalStateException("해당 테마를 사용하는 예약이 존재합니다.");
        }

        themeRepository.removeById(id);
    }
}
