package roomescape.theme.application.query;

import java.util.List;
import roomescape.theme.domain.PopularThemePeriod;

public interface PopularThemeDao {
    List<PopularThemeResult> findTop10PopularThemes(PopularThemePeriod period);
}
