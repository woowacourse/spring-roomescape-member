package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.controller.theme.CreateThemeRequest;
import roomescape.controller.theme.PopularThemeRequest;
import roomescape.controller.theme.PopularThemeResponse;
import roomescape.controller.theme.ThemeResponse;
import roomescape.domain.Theme;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ThemeRepository;
import roomescape.service.exception.DaysLimitException;
import roomescape.service.exception.RowsLimitException;
import roomescape.service.exception.ThemeNotFoundException;
import roomescape.service.exception.ThemeUsedException;

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

    private static void validateNotFound(int deletedCount) {
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

    private void validateUsed(Long id) {
        if (reservationRepository.existsByThemeId(id)) {
            throw new ThemeUsedException("예약된 테마는 삭제할 수 없습니다.");
        }
    }

    public List<PopularThemeResponse> getPopularThemes(PopularThemeRequest popularThemeRequest) {
        validateDaysLimit(popularThemeRequest.days());
        validateRowsLimit(popularThemeRequest.limit());
        final LocalDate fromDate = getFromDate(popularThemeRequest.days());
        final LocalDate toDate = getToDate();
        final List<Theme> popularThemes = findPopularThemes(popularThemeRequest, fromDate, toDate);

        return popularThemes.stream()
                .map(PopularThemeResponse::from)
                .toList();
    }

    private void validateDaysLimit(int days) {
        if (days > DAYS_LIMIT) {
            throw new DaysLimitException("기간은 " + DAYS_LIMIT + "일을 넘을 수 없습니다.");
        }
    }

    private void validateRowsLimit(int limit) {
        if (limit > ROWS_LIMIT) {
            throw new RowsLimitException("조회할 최대 개수는 " + ROWS_LIMIT + "개를 넘을 수 없습니다.");
        }
    }

    private LocalDate getFromDate(int days) {
        return LocalDate.now().minusDays(days);
    }

    private LocalDate getToDate() {
        return LocalDate.now().minusDays(-1);
    }

    private List<Theme> findPopularThemes(PopularThemeRequest popularThemeRequest, LocalDate fromDate, LocalDate toDate) {
        return themeRepository.findPopularThemes(fromDate, toDate, popularThemeRequest.limit());
    }
}
