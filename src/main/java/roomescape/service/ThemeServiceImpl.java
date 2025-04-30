package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Theme;
import roomescape.dto.PopularThemeResponse;
import roomescape.dto.ThemeRequest;
import roomescape.dto.ThemeResponse;
import roomescape.entity.ThemeEntity;
import roomescape.exception.theme.ThemeNotFoundException;
import roomescape.repository.reservation.ReservationRepository;
import roomescape.repository.theme.ThemeRepository;

@Service
public class ThemeServiceImpl implements ThemeService {
    private final ThemeRepository themeRepository;
    private final ReservationRepository reservationRepository;

    public ThemeServiceImpl(ThemeRepository themeRepository, ReservationRepository reservationRepository) {
        this.themeRepository = themeRepository;
        this.reservationRepository = reservationRepository;
    }

    @Override
    public ThemeResponse create(ThemeRequest request) {
        Theme theme = request.toEntity();
        return ThemeResponse.from(themeRepository.add(theme));
    }

    @Override
    public List<ThemeResponse> getAll() {
        List<ThemeEntity> themes = themeRepository.findAll();
        return ThemeResponse.from(themes);
    }

    @Override
    public void deleteById(Long id) {
        int affectedCount = themeRepository.deleteById(id);
        if (affectedCount == 0) {
            throw new ThemeNotFoundException(id);
        }
    }

    @Override
    public List<PopularThemeResponse> getPopularThemes() {
        LocalDate endDate = LocalDate.now().minusDays(1);
        LocalDate startDate = endDate.minusDays(7);
        List<Long> themeIds = reservationRepository.findTopThemesByReservationCountBetween(startDate, endDate);

        return themeIds.stream().map(themeId -> {
            ThemeEntity themeEntity = themeRepository.findById(themeId)
                    .orElseThrow(() -> new ThemeNotFoundException(themeId));
            return PopularThemeResponse.of(themeEntity);
        }).toList();
    }
}
