package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.controller.theme.CreateThemeRequest;
import roomescape.controller.theme.CreateThemeResponse;
import roomescape.controller.theme.PopularThemeRequest;
import roomescape.controller.theme.PopularThemeResponse;
import roomescape.domain.Theme;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ThemeRepository;
import roomescape.service.exception.ThemeNotFoundException;
import roomescape.service.exception.ThemeUsedException;

import java.time.LocalDate;
import java.util.List;

@Service
public class ThemeService {

    private final ReservationRepository reservationRepository;
    private final ThemeRepository themeRepository;

    public ThemeService(final ReservationRepository reservationRepository, final ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.themeRepository = themeRepository;
    }

    public List<CreateThemeResponse> getThemes() {
        return themeRepository.findAll().stream()
                .map(CreateThemeResponse::from)
                .toList();
    }

    public CreateThemeResponse addTheme(final CreateThemeRequest createThemeRequest) {
        final Theme theme = createThemeRequest.toDomain();
        final Theme savedTheme = themeRepository.save(theme);
        return CreateThemeResponse.from(savedTheme);
    }

    public int deleteTheme(final Long id) {
        if (reservationRepository.existsByThemeId(id)) {
            throw new ThemeUsedException("예약된 테마는 삭제할 수 없습니다.");
        }
        final int deletedCount = themeRepository.delete(id);
        if (deletedCount == 0) {
            throw new ThemeNotFoundException("테마가 존재하지 않습니다.");
        }
        return deletedCount;
    }

    public List<PopularThemeResponse> getPopularThemes(PopularThemeRequest popularThemeRequest) {
        // TODO: validate days limit
        final LocalDate startFilterDate = LocalDate.now().minusDays(popularThemeRequest.days());
        final LocalDate endFilterDate = LocalDate.now().minusDays(-1);

        final List<Theme> popularThemes = themeRepository.findPopularThemes(
                startFilterDate,
                endFilterDate,
                popularThemeRequest.limit()
        );
        return popularThemes.stream()
                .map(PopularThemeResponse::from)
                .toList();
    }
}
