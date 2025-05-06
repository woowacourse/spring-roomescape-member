package roomescape.service.theme.popular;

import java.util.List;
import roomescape.dto.theme.PopularThemeResponse;

public interface PopularThemeUseCase {
    List<PopularThemeResponse> getPopularThemes();
}
