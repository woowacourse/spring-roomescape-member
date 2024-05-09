package roomescape.reservation.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservation.domain.Theme;
import roomescape.reservation.dto.response.ThemeResponse;
import roomescape.global.exception.NotFoundException;
import roomescape.reservation.domain.ThemeRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class ThemeService {
    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public ThemeResponse create(Theme theme) {
        Theme savedTheme = themeRepository.save(theme);
        return ThemeResponse.from(savedTheme);
    }

    @Transactional(readOnly = true)
    public List<ThemeResponse> findAll() {
        List<Theme> themes = themeRepository.findAll();
        return themes.stream()
                .map(ThemeResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public Theme findById(Long id) {
        return themeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("id 에 해당하는 테마가 없습니다."));
    }

    public void deleteById(Long id) {
        themeRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<ThemeResponse> findAllPopular() {
        LocalDate startDate = LocalDate.now().minusDays(7);
        LocalDate endDate = LocalDate.now().minusDays(1);
        List<Theme> allOrderByReservationCountInLastWeek = themeRepository.findAllByDateBetweenAndOrderByReservationCount(startDate, endDate, 10);
        return allOrderByReservationCountInLastWeek.stream()
                .map(ThemeResponse::from)
                .toList();
    }
}
