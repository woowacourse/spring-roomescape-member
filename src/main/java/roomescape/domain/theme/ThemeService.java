package roomescape.domain.theme;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.domain.theme.dto.ThemeResponse;

@Service
@RequiredArgsConstructor
public class ThemeService {

    private final JdbcThemeRepository jdbcThemeRepository;

    public List<ThemeResponse> getAllTheme() {
        return jdbcThemeRepository.findAll().stream()
            .map(ThemeResponse::from)
            .toList();
    }
}
