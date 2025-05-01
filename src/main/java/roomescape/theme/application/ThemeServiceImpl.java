package roomescape.theme.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.reservation.domain.ReservationDate;
import roomescape.theme.application.converter.ThemeConverter;
import roomescape.theme.application.usecase.ThemeCommandUseCase;
import roomescape.theme.application.usecase.ThemeQueryUseCase;
import roomescape.theme.domain.ThemeId;
import roomescape.theme.ui.dto.CreateThemeWebRequest;
import roomescape.theme.ui.dto.ThemeResponse;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ThemeServiceImpl implements ThemeService {

    private final ThemeQueryUseCase themeQueryUseCase;
    private final ThemeCommandUseCase themeCommandUseCase;

    @Override
    public List<ThemeResponse> getAll() {
        return ThemeConverter.toDto(themeQueryUseCase.getAll());
    }

    @Override
    public List<ThemeResponse> getRanking() {
        // TODO 인자로 받기
        final int dateRange = 7;
        final LocalDate endDate = LocalDate.now();
        final LocalDate startDate = endDate.minusDays(dateRange);
        final int count = 10;

        return ThemeConverter.toDto(
                themeQueryUseCase.getRanking(
                        ReservationDate.from(startDate),
                        ReservationDate.from(endDate),
                        count));
    }

    @Override
    public ThemeResponse create(final CreateThemeWebRequest createThemeWebRequest) {
        return ThemeConverter.toDto(themeCommandUseCase.create(ThemeConverter.toServiceDto(createThemeWebRequest)));
    }

    @Override
    public void delete(final Long id) {
        themeCommandUseCase.delete(ThemeId.from(id));
    }
}
