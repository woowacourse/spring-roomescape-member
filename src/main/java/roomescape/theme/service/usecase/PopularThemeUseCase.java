package roomescape.theme.service.usecase;

import java.util.List;
import roomescape.theme.dto.PopularThemeResponse;

public interface PopularThemeUseCase {
    List<PopularThemeResponse> getPopularThemes();
}
