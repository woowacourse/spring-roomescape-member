package roomescape.theme.application.query;

import java.util.List;
import roomescape.theme.domain.PopularThemePeriod;

public interface PopularThemeDao {
    List<PopularTheme> findTop10PopularThemes(PopularThemePeriod period);
}
