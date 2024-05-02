package roomescape.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Theme;
import roomescape.dto.ThemeResponse;
import roomescape.exception.NotFoundException;
import roomescape.repository.ThemeRepository;

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
    public ThemeResponse findById(Long id) {
        Theme theme = themeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id + "에 해당하는 테마가 없습니다."));
        return ThemeResponse.from(theme);
    }

    public void deleteById(Long id) {
        Theme theme = themeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id + "에 해당하는 테마가 없습니다."));
        themeRepository.deleteById(theme.getId());
    }

    @Transactional(readOnly = true)
    public List<ThemeResponse> findAllPopular() {
        List<Theme> allOrderByReservationCountInLastWeek = themeRepository.findAllOrderByReservationCountInLastWeek();
        return allOrderByReservationCountInLastWeek.stream()
                .map(ThemeResponse::from)
                .toList();
    }
}
