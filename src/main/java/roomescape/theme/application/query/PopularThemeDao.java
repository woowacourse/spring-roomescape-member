package roomescape.theme.application.query;

import java.time.LocalDate;
import java.util.List;

public interface PopularThemeDao {
    List<PopularTheme> findTop10PopularThemesBetween(LocalDate from, LocalDate to);
}
