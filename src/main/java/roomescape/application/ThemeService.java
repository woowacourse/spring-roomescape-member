package roomescape.application;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.application.dto.request.ThemeRequest;
import roomescape.application.dto.response.ThemeResponse;
import roomescape.application.exception.EntityReferenceOnDeleteException;
import roomescape.domain.Theme;
import roomescape.domain.ThemeRepository;

@Service
public class ThemeService {
    private final ThemeRepository themeRepository;
    private final Clock clock;

    public ThemeService(ThemeRepository themeRepository, Clock clock) {
        this.themeRepository = themeRepository;
        this.clock = clock;
    }

    public ThemeResponse create(ThemeRequest request) {
        Theme savedTheme = themeRepository.create(request.toTheme());
        return ThemeResponse.from(savedTheme);
    }

    public List<ThemeResponse> findAll() {
        return themeRepository.findAll()
                .stream()
                .map(ThemeResponse::from)
                .toList();
    }

    @Transactional
    public void deleteById(long id) {
        Theme theme = themeRepository.getById(id);
        if (themeRepository.existsByTimeId(id)) {
            throw new EntityReferenceOnDeleteException(String.format(
                    "해당 테마와 연관된 예약이 존재하여 삭제할 수 없습니다. 삭제 요청한 테마:%s",
                    theme.getName())
            );
        }
        themeRepository.deleteById(theme.getId());
    }

    public List<ThemeResponse> findPopularThemes() {
        LocalDate today = LocalDate.now(clock);
        return themeRepository.findPopularThemesDateBetween(
                        today.minusDays(7),
                        today.minusDays(1),
                        10)
                .stream()
                .map(ThemeResponse::from)
                .toList();
    }
}
