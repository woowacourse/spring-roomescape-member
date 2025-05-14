package roomescape.theme.service.usecase;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.dto.PopularThemeResponse;
import roomescape.theme.exception.ThemeNotFoundException;
import roomescape.theme.repository.ThemeRepository;

@Service
public class PopularThemeService implements PopularThemeUseCase {
    private final ThemeRepository themeRepository;
    private final ReservationRepository reservationRepository;

    public PopularThemeService(ThemeRepository themeRepository, ReservationRepository reservationRepository) {
        this.themeRepository = themeRepository;
        this.reservationRepository = reservationRepository;
    }

    @Override
    public List<PopularThemeResponse> getPopularThemes() {
        LocalDate endDate = LocalDate.now().minusDays(1);
        LocalDate startDate = endDate.minusDays(7);
        List<Long> themeIds = reservationRepository.findThemeIdsOrderByReservationCountBetween(startDate, endDate, 10)
                .stream()
                .toList();

        return themeIds.stream().map(themeId -> {
            Theme theme = themeRepository.findById(themeId)
                    .orElseThrow(ThemeNotFoundException::new);
            return PopularThemeResponse.of(theme);
        }).toList();
    }
}
