package roomescape.theme.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.dto.ThemeReqDto;
import roomescape.theme.domain.dto.ThemeResDto;
import roomescape.theme.repository.ThemeRepository;

@Service
public class ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public ThemeResDto add(ThemeReqDto dto) {
        Theme notSavedTheme = new Theme(dto.name(), dto.description(), dto.thumbnail());
        Theme savedTheme = themeRepository.add(notSavedTheme);
        return ThemeResDto.from(savedTheme);
    }

    public List<ThemeResDto> findAll() {
        return themeRepository.findAll().stream()
            .map(ThemeResDto::from)
            .toList();
    }
}
