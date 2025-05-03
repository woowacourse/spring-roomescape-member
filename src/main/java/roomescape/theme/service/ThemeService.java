package roomescape.theme.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.common.util.DateTime;
import roomescape.reservation.domain.ReservationRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.ThemeRepository;
import roomescape.theme.dto.PopularThemeResponse;
import roomescape.theme.dto.ThemeRequest;
import roomescape.theme.dto.ThemeResponse;

@Service
public class ThemeService {

    private static final int POPULAR_THEME_COUNT = 10;

    private final DateTime dateTime;
    private final ThemeRepository themeRepository;
    private final ReservationRepository reservationRepository;

    public ThemeService(final DateTime dateTime,
                        final ThemeRepository themeRepository,
                        final ReservationRepository reservationRepository) {
        this.dateTime = dateTime;
        this.themeRepository = themeRepository;
        this.reservationRepository = reservationRepository;
    }

    public ThemeResponse createTheme(final ThemeRequest request) {
        Theme theme = Theme.createWithoutId(request.name(), request.description(), request.thumbnail());
        Long id = themeRepository.save(theme);

        return ThemeResponse.from(theme.assignId(id));
    }

    public void deleteThemeById(final Long id) {
        if (reservationRepository.existByThemeId(id)) {
            throw new IllegalArgumentException("예약한 기록이 존재하여 삭제할 수 없습니다.");
        }
        boolean isDeleted = themeRepository.deleteBy(id);
        validateIsExistsReservationTimeId(isDeleted);
    }

    private void validateIsExistsReservationTimeId(boolean isDeleted) {
        if (!isDeleted) {
            throw new IllegalArgumentException("존재하지 않는 테마입니다.");
        }
    }

    public List<ThemeResponse> getThemes() {
        return themeRepository.findAll().stream()
                .map(ThemeResponse::from)
                .toList();
    }

    public List<PopularThemeResponse> getPopularThemes() {
        LocalDate now = dateTime.now().toLocalDate();

        LocalDate start = now.minusDays(8);
        LocalDate end = now.minusDays(1);

        return themeRepository.findPopularThemes(start, end, POPULAR_THEME_COUNT).stream()
                .map(theme -> new PopularThemeResponse(theme.getName(), theme.getThumbnail(), theme.getDescription()))
                .toList();
    }
}
