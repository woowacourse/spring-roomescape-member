package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.ReservationRepository;
import roomescape.domain.Theme;
import roomescape.domain.ThemeRepository;
import roomescape.dto.ThemeRequest;

@Service
@Transactional(readOnly = true)
public class ThemeService {

    private final ThemeRepository themeRepository;
    private final ReservationRepository reservationRepository;

    public ThemeService(ThemeRepository themeRepository, ReservationRepository reservationRepository) {
        this.themeRepository = themeRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<Theme> getThemes() {
        return themeRepository.findAll();
    }

    public Theme getTheme(Long id) {
        return themeRepository.getById(id);
    }

    public List<Theme> getPopularTop10Themes(LocalDate now, Integer days) {
        LocalDate start = now.minusDays(days);
        LocalDate end = now.minusDays(1);
        return themeRepository.getPopularTop10Themes(start, end);
    }

    @Transactional
    public Theme addTheme(ThemeRequest request) {
        Long id = themeRepository.save(
                new Theme(
                        request.name(),
                        request.description(),
                        request.thumbnailImageUrl()));
        return getTheme(id);
    }

    @Transactional
    public void deleteTheme(Long id) {
        if (reservationRepository.existsByThemeId(id)) {
            throw new IllegalArgumentException("해당 테마를 사용 중인 예약이 존재하여 삭제할 수 없습니다.");
        }
        themeRepository.deleteById(id);
    }
}
