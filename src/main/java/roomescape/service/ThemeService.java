package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.PopularThemesCriteria;
import roomescape.domain.Theme;
import roomescape.persistence.ReservationRepository;
import roomescape.persistence.ThemeRepository;
import roomescape.service.request.ThemeRequest;
import roomescape.service.response.ThemeResponse;

@Service
public class ThemeService {

    private final ReservationRepository reservationRepository;
    private final ThemeRepository themeRepository;
    private final PopularThemesCriteria popularThemesCriteria;

    public ThemeService(ReservationRepository reservationRepository, ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.themeRepository = themeRepository;
        this.popularThemesCriteria = new PopularThemesCriteria();
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

        if (!themeRepository.removeById(id)) {
            throw new IllegalArgumentException("존재하지 않는 테마입니다. id를 확인하세요.");
        }
    }

    public List<ThemeResponse> findAll(boolean showRanking) {
        if (showRanking) {
            return findPopularThemes();
        }
        return themeRepository.findAll().stream()
                .map(ThemeResponse::from)
                .toList();
    }

    private List<ThemeResponse> findPopularThemes() {
        LocalDate startDate = popularThemesCriteria.getStartDate();
        LocalDate endDate = popularThemesCriteria.getEndDate();
        int countLimit = popularThemesCriteria.getCountLimit();

        return themeRepository.findOrderedByReservationCountInPeriod(startDate, endDate, countLimit).stream()
                .map(ThemeResponse::from)
                .toList();
    }
}
