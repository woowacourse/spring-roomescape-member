package roomescape.domain.theme;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import roomescape.domain.theme.dto.CreateThemeRequest;
import roomescape.domain.theme.dto.CreateThemeResponse;
import roomescape.domain.theme.dto.ThemeResponse;

@Slf4j
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

    // TODO: Reservation에 Theme 연결 후 Theme에 연결된 예약 존재 시 삭제 불가 로직 필요.
    public void deleteTheme(Long id) {
        int deletedCount = jdbcThemeRepository.deleteById(id);
        if (deletedCount == 0) {
            log.warn("삭제할 테마가 존재하지 않습니다. themeId = {}", id);
        }
    }
}
