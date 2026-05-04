package roomescape.domain.theme;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.domain.theme.dto.CreateThemeRequest;
import roomescape.domain.theme.dto.CreateThemeResponse;
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

    public CreateThemeResponse createTheme(CreateThemeRequest request) {
        Theme theme = jdbcThemeRepository.save(request.toEntity());
        return CreateThemeResponse.from(theme);
    }
}
