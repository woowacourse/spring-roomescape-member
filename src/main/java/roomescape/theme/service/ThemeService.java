package roomescape.theme.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.theme.domain.Theme;
import roomescape.global.exception.InvalidRequestException;
import roomescape.theme.repository.ThemeRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class ThemeService {
    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    @Transactional
    public Theme create(String name, String description, String thumbnail) {
        validateNotDuplicated(name);
        Theme theme = new Theme(name, description, thumbnail);

        return themeRepository.save(theme);
    }

    private void validateNotDuplicated(String name) {
        if (themeRepository.existsByName(name)) {
            throw new InvalidRequestException("이미 존재하는 테마 이름입니다.");
        }
    }

    @Transactional(readOnly = true)
    public List<Theme> list(){
        return themeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Theme> findPopularThemes(int days, LocalDate now, int size){
        LocalDate startDate = now.minusDays(days);
        LocalDate endDate = now.minusDays(1);

        return themeRepository.findTopThemesByReservationCount(startDate, endDate, size);
    }

    @Transactional
    public void delete(Long id) {
        themeRepository.deleteById(id);
    }
}
