package roomescape.admin.theme;

import org.springframework.stereotype.Service;
import roomescape.admin.theme.dto.AdminThemeRequest;
import roomescape.admin.theme.dto.AdminThemeResponse;
import roomescape.admin.theme.dto.AdminThemesResponse;
import roomescape.user.reservation.ReservationRepository;

import java.util.List;
import roomescape.user.theme.Theme;

@Service
public class AdminThemeService {

    private final AdminThemeRepository adminThemeRepository;
    private final ReservationRepository reservationRepository;

    public AdminThemeService(
        AdminThemeRepository adminThemeRepository,
        ReservationRepository reservationRepository
    ) {
        this.adminThemeRepository = adminThemeRepository;
        this.reservationRepository = reservationRepository;
    }

    public AdminThemeResponse createTheme(AdminThemeRequest request) {
        validateDuplicateTheme(request.name());
        Theme theme = Theme.of(
            request.name(),
            request.description(),
            request.imageUrl()
        );

        Theme saved = adminThemeRepository.save(theme);
        return AdminThemeResponse.from(saved);
    }

    private void validateDuplicateTheme(String name) {
        if (adminThemeRepository.existsByName(name)) {
            throw new IllegalArgumentException("[ERROR] 이미 존재하는 테마 이름입니다.");
        }
    }

    public AdminThemesResponse getAllThemes() {
        List<Theme> themes = adminThemeRepository.findAll();

        return AdminThemesResponse.from(themes);
    }

    public void deleteTheme(Long id) {
        if (!adminThemeRepository.existsById(id)) {
            throw new IllegalArgumentException("[ERROR] 존재하지 않는 theme id 입니다.");
        }
        if (reservationRepository.existsByThemeId(id)) {
            throw new IllegalArgumentException("[ERROR] 예약이 존재하는 테마는 삭제할 수 없습니다.");
        }
        adminThemeRepository.deleteById(id);
    }
}
