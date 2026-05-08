package roomescape.service.theme;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.repository.reservation.ReservationRepository;
import roomescape.domain.theme.Theme;
import roomescape.repository.theme.ThemeRepository;

@Service
public class ThemeService {

    private final ThemeRepository themeRepository;
    private final ReservationRepository reservationRepository;

    public ThemeService(
            final ThemeRepository themeRepository,
            final ReservationRepository reservationRepository
    ) {
        this.themeRepository = themeRepository;
        this.reservationRepository = reservationRepository;
    }

    public Theme save(final String name, final String description, final String thumbnailUrl) {
        Theme nonIdTheme = Theme.createNew(name, description, thumbnailUrl);

        if (themeRepository.existsByName(name)) {
            throw new IllegalArgumentException("[ERROR] 테마 이름 중복은 불가능합니다.");
        }

        return themeRepository.save(nonIdTheme);
    }

    public List<Theme> getAll() {
        return themeRepository.findAll();
    }

    public Theme getById(final long themeId) {
        return themeRepository.findById(themeId)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 테마를 찾을 수 없습니다."));
    }

    public void deleteById(final long themeId) {
        if (reservationRepository.existsByThemeId(themeId)) {
            throw new IllegalArgumentException("[ERROR] 이미 예약된 테마는 삭제할 수 없습니다.");
        }

        themeRepository.deleteById(themeId);
    }

    public List<Theme> getPopularThemes(final int period, final int limit) {
        return themeRepository.findPopularThemes(period, limit);
    }
}
