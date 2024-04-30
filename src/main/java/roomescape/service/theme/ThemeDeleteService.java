package roomescape.service.theme;

import org.springframework.stereotype.Service;
import roomescape.repository.ThemeRepository;

@Service
public class ThemeDeleteService {

    private final ThemeRepository themeRepository;

    public ThemeDeleteService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public void deleteTheme(Long id) {
        themeRepository.findAll()
                .stream()
                .filter(theme -> theme.isSameTheme(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테마 아이디 입니다."));

        themeRepository.deleteById(id);
    }
}
