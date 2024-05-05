package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.ReservationRepository;
import roomescape.domain.Theme;
import roomescape.domain.ThemeRepository;
import roomescape.service.dto.ThemeRequest;
import roomescape.service.dto.ThemeResponse;

@Service
public class ThemeService {

    private final ThemeRepository themeRepository;
    private final ReservationRepository reservationRepository;

    public ThemeService(ThemeRepository themeRepository, ReservationRepository reservationRepository) {
        this.themeRepository = themeRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<ThemeResponse> findAllThemes() {
        return themeRepository.findAllThemes()
                .stream()
                .map(ThemeResponse::new)
                .toList();
    }

    public List<ThemeResponse> findTopBookedThemes() {
        List<Theme> topBookedThemes = themeRepository.findTopBookedThemes(
                LocalDate.now().minusDays(7),
                LocalDate.now().minusDays(1),
                10);

        return topBookedThemes.stream()
                .map(ThemeResponse::new)
                .toList();
    }

    public ThemeResponse createTheme(ThemeRequest requestDto) {
        Theme theme = themeRepository.insertTheme(requestDto.toTheme());
        return new ThemeResponse(theme);
    }

    public void deleteTheme(long id) {
        if (!themeRepository.isExistThemeOf(id)) {
            throw new IllegalArgumentException("존재하지 않는 아이디입니다.");
        }
        if (reservationRepository.hasReservationOfThemeId(id)) {
            throw new IllegalArgumentException("해당 테마에 예약이 있어 삭제할 수 없습니다.");
        }
        themeRepository.deleteThemeById(id);
    }
}
