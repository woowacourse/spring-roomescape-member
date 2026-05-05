package roomescape.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.domain.Theme;
import roomescape.domain.dto.ThemeCreateData;
import roomescape.repository.ThemeDao;
import roomescape.service.dto.request.ThemeCreateRequest;
import roomescape.service.dto.response.ThemeResponse;

@Service
@RequiredArgsConstructor
public class ThemeService {

    private final ThemeDao themeDao;

    public ThemeResponse create(final ThemeCreateRequest request) {
        final Theme themeWithoutId = Theme.create(
                new ThemeCreateData(
                        request.name(),
                        request.description(),
                        request.thumbnailUrl()
                )
        );

        Theme theme = themeDao.save(themeWithoutId);

        return ThemeResponse.from(theme);
    }

    public void delete(final Long themeId) {
        boolean deleted = themeDao.deleteById(themeId);

        if (!deleted) {
            throw new IllegalArgumentException("존재하지 않는 테마입니다.");
        }
    }
}
