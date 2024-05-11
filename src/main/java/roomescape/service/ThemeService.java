package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Theme;
import roomescape.repository.JdbcReservationRepository;
import roomescape.repository.JdbcThemeRepository;
import roomescape.service.dto.PopularThemeRequest;
import roomescape.service.dto.ThemeRequest;
import roomescape.service.dto.ThemeResponse;

@Service
public class ThemeService {

    private final JdbcThemeRepository themeRepository;
    private final JdbcReservationRepository reservationRepository;

    public ThemeService(JdbcThemeRepository themeRepository, JdbcReservationRepository reservationRepository) {
        this.themeRepository = themeRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<ThemeResponse> findAllThemes() {
        return themeRepository.findAllThemes()
                .stream()
                .map(ThemeResponse::new)
                .toList();
    }

    public List<ThemeResponse> findTopBookedThemes(PopularThemeRequest requestDto) {
        List<Theme> topBookedThemes = themeRepository.findTopThemesDescendingByReservationCount(
                requestDto.getStartDate(), requestDto.getEndDate(), requestDto.getCount());

        return topBookedThemes.stream()
                .map(ThemeResponse::new)
                .toList();
    }

    public ThemeResponse createTheme(ThemeRequest requestDto) {
        Theme theme = themeRepository.insertTheme(requestDto.toTheme());
        return new ThemeResponse(theme);
    }

    public void deleteTheme(long id) {
        if (!themeRepository.isThemeExistsById(id)) {
            throw new IllegalArgumentException("존재하지 않는 아이디입니다.");
        }
        if (reservationRepository.isReservationExistsByThemeId(id)) {
            throw new IllegalArgumentException("해당 테마에 예약이 있어 삭제할 수 없습니다.");
        }
        themeRepository.deleteThemeById(id);
    }
}
