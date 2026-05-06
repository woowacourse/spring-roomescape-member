package roomescape.domain.theme;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import roomescape.domain.reservation.ReservationRepository;
import roomescape.domain.theme.dto.AdminThemeResponse;
import roomescape.domain.theme.dto.CreateThemeRequest;
import roomescape.domain.theme.dto.CreateThemeResponse;
import roomescape.domain.theme.dto.ThemeRankResponse;
import roomescape.domain.theme.dto.ThemeResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class ThemeService {

    private static final int RANK_LIMIT = 10;
    private static final int RANK_DAYS_LIMIT = 7;

    private final ThemeRepository themeRepository;
    private final ReservationRepository reservationRepository;

    public List<AdminThemeResponse> getAllThemeForAdmin() {
        return themeRepository.findAll().stream()
            .map(AdminThemeResponse::from)
            .toList();
    }

    public CreateThemeResponse createTheme(CreateThemeRequest request) {
        Theme theme = themeRepository.save(request.toEntity());
        return CreateThemeResponse.from(theme);
    }

    // TODO: Reservation에 Theme 연결 후 Theme에 연결된 예약 존재 시 삭제 불가 로직 필요.
    public void deleteTheme(Long id) {
        int deletedCount = themeRepository.deleteById(id);
        if (deletedCount == 0) {
            log.warn("삭제할 테마가 존재하지 않습니다. themeId = {}", id);
        }
    }

    public List<ThemeResponse> getAllTheme() {
        return themeRepository.findAll().stream()
            .map(ThemeResponse::from)
            .toList();
    }

    public List<ThemeRankResponse> getThemeRank() {
        LocalDate today = LocalDate.now();
        LocalDate startDay = today.minusDays(RANK_DAYS_LIMIT);
        List<Theme> populateThemes = reservationRepository.findPopularThemes(RANK_LIMIT, startDay, today);
        return populateThemes.stream()
            .map(ThemeRankResponse::from)
            .toList();
    }
}
