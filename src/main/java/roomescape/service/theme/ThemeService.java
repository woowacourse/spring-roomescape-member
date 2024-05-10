package roomescape.service.theme;

import org.springframework.stereotype.Service;
import roomescape.controller.theme.CreateThemeRequest;
import roomescape.controller.theme.PopularThemeRequest;
import roomescape.controller.theme.PopularThemeResponse;
import roomescape.controller.theme.ThemeResponse;
import roomescape.domain.Theme;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ThemeRepository;
import roomescape.repository.exception.ThemeNotFoundException;
import roomescape.service.theme.exception.DaysLimitException;
import roomescape.service.theme.exception.RowsLimitException;
import roomescape.service.theme.exception.ThemeUsedException;

import java.time.LocalDate;
import java.util.List;

@Service
public class ThemeService {

    public static final int DAYS_LIMIT = 30;
    private static final int ROWS_LIMIT = 100;
    private final ReservationRepository reservationRepository;
    private final ThemeRepository themeRepository;

    public ThemeService(final ReservationRepository reservationRepository, final ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.themeRepository = themeRepository;
    }

    private static void validateNotFound(final int deletedCount) {
        if (deletedCount == 0) {
            throw new ThemeNotFoundException("테마가 존재하지 않습니다.");
        }
    }

    public List<ThemeResponse> getThemes() {
        return themeRepository.findAll().stream()
                .map(ThemeResponse::from)
                .toList();
    }

    public ThemeResponse addTheme(final CreateThemeRequest createThemeRequest) {
        final Theme theme = createThemeRequest.toDomain();
        final Theme savedTheme = themeRepository.save(theme);

        return ThemeResponse.from(savedTheme);
    }

    public int deleteTheme(final Long id) {
        validateUsed(id);

        final int deletedCount = themeRepository.delete(id);
        validateNotFound(deletedCount);

        return deletedCount;
    }

    private void validateUsed(final Long id) {
        if (reservationRepository.existsByThemeId(id)) {
            throw new ThemeUsedException("예약된 테마는 삭제할 수 없습니다.");
        }
    }

    public List<PopularThemeResponse> getPopularThemes(final PopularThemeRequest popularThemeRequest) {
        validateDaysLimit(popularThemeRequest.days());
        validateRowsLimit(popularThemeRequest.limit());
        final LocalDate fromDate = getFromDate(popularThemeRequest.days());
        final LocalDate toDate = getToDate();
        final List<Theme> popularThemes = findPopularThemes(popularThemeRequest, fromDate, toDate);

        return popularThemes.stream()
                .map(PopularThemeResponse::from)
                .toList();
    }

    private void validateDaysLimit(final int days) {
        if (days > DAYS_LIMIT) {
            throw new DaysLimitException("기간은 " + DAYS_LIMIT + "일을 넘을 수 없습니다.");
        }
    }

    private void validateRowsLimit(final int limit) {
        if (limit > ROWS_LIMIT) {
            throw new RowsLimitException("조회할 최대 개수는 " + ROWS_LIMIT + "개를 넘을 수 없습니다.");
        }
    }

    private LocalDate getFromDate(final int days) {
        return LocalDate.now().minusDays(days);
    }

    private LocalDate getToDate() {
        return LocalDate.now().minusDays(-1);
    }

    private List<Theme> findPopularThemes(PopularThemeRequest popularThemeRequest, LocalDate fromDate, LocalDate toDate) {
        return themeRepository.findPopularThemes(fromDate, toDate, popularThemeRequest.limit());
    }
}
