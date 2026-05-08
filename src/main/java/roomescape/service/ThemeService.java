package roomescape.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Theme;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ThemeRepository;
import roomescape.repository.dto.PopularThemeDto;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ThemeService {

    private final ThemeRepository themeRepository;
    private final ReservationRepository reservationRepository;

    public ThemeService(ThemeRepository themeRepository, ReservationRepository reservationRepository) {
        this.themeRepository = themeRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<Theme> findAll() {
        return themeRepository.findAll();
    }

    @Transactional
    public Theme create(String name, String description, String thumbnail) {
        Theme theme = new Theme(null, name, description, thumbnail);
        Long id = themeRepository.insert(theme);
        return themeRepository.findBy(id)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 존재하지 않는 테마입니다."));
    }

    @Transactional
    public void delete(Long id) {
        validateId(id);
        validateDeletable(id);
        themeRepository.delete(id);
    }

    public List<PopularThemeDto> findWeeklyTopTen() {
        LocalDate startDate = LocalDate.now().minusWeeks(1);
        LocalDate endDate = startDate.plusDays(6);
        return themeRepository.findPopular(startDate, endDate, 10);
    }

    private void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("[ERROR] id는 양수이어야 합니다.");
        }
    }

    private void validateDeletable(Long id) {
        if (reservationRepository.existsByThemeId(id)) {
            throw new IllegalArgumentException("[ERROR] 예약이 존재하는 테마는 삭제할 수 없습니다.");
        }
    }
}
