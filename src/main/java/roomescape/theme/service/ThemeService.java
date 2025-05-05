package roomescape.theme.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.reservation.domain.ReservationDate;
import roomescape.theme.service.converter.ThemeConverter;
import roomescape.theme.domain.ThemeId;
import roomescape.theme.controller.dto.CreateThemeWebRequest;
import roomescape.theme.controller.dto.ThemeResponse;
import roomescape.theme.service.usecase.ThemeCommandUseCase;
import roomescape.theme.service.usecase.ThemeQueryUseCase;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ThemeService {

    private final ThemeQueryUseCase themeQueryUseCase;
    private final ThemeCommandUseCase themeCommandUseCase;

    public List<ThemeResponse> getAll() {
        return ThemeConverter.toDto(themeQueryUseCase.getAll());
    }

    public List<ThemeResponse> getRanking() {
        final int count = 10;
        final int dateRange = 7;
        final LocalDate endDate = LocalDate.now();
        final LocalDate startDate = endDate.minusDays(dateRange);

        return ThemeConverter.toDto(
                themeQueryUseCase.getRanking(
                        ReservationDate.from(startDate),
                        ReservationDate.from(endDate),
                        count));
    }

    public ThemeResponse create(final CreateThemeWebRequest createThemeWebRequest) {
        return ThemeConverter.toDto(themeCommandUseCase.create(ThemeConverter.toServiceDto(createThemeWebRequest)));
    }

    public void delete(final Long id) {
        themeCommandUseCase.delete(ThemeId.from(id));
    }
}
