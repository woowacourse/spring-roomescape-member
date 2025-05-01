package roomescape.theme.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.theme.controller.dto.ThemeRankingResponse;
import roomescape.theme.controller.dto.ThemeRequest;
import roomescape.theme.controller.dto.ThemeResponse;
import roomescape.theme.domain.Theme;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.repository.ThemeRepository;

@Service
public class ThemeService {

    private static final int DAYS_BEFORE_START = 7;
    private static final int DAYS_BEFORE_END = 1;
    private static final int RANKING_LIMIT = 10;
    private final ThemeRepository themeRepository;
    private final ReservationRepository reservationRepository;

    public ThemeService(ThemeRepository themeRepository, ReservationRepository reservationRepository) {
        this.themeRepository = themeRepository;
        this.reservationRepository = reservationRepository;
    }

    public ThemeResponse add(ThemeRequest request) {
        Theme themeWithoutId = request.toThemeWithoutId();
        Long id = themeRepository.saveAndReturnId(themeWithoutId);

        Theme theme = themeWithoutId.withId(id);
        return ThemeResponse.from(theme);
    }

    public List<ThemeResponse> getThemes() {
        return themeRepository.findAll().stream()
                .map(ThemeResponse::from)
                .toList();
    }

    public void remove(Long id) {
        if (reservationRepository.existReservationByThemeId(id)) {
            throw new IllegalStateException("해당 테마에 대한 예약이 존재합니다.");
        }
        themeRepository.deleteById(id);
    }

    public List<ThemeRankingResponse> getThemeRankings() {
        LocalDate startDate = LocalDate.now().minusDays(DAYS_BEFORE_START);
        LocalDate endDate =  LocalDate.now().minusDays(DAYS_BEFORE_END);
        List<Theme> themeRankings = themeRepository.findByPeriodAndLimit(startDate, endDate, RANKING_LIMIT);
        return themeRankings.stream()
                .map(ThemeRankingResponse::from)
                .toList();
    }
}
