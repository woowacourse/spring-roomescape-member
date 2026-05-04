package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.Theme;
import roomescape.dto.theme.ThemeRequestDto;

@Service
public class ThemeService {

    public Theme addTheme(ThemeRequestDto themeRequest) {
        // TODO: 저장
        return new Theme(1L, "", "", "");
    }
}
