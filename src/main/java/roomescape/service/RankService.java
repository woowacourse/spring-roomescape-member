package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.Theme;
import roomescape.domain.dto.ThemeResponse;
import roomescape.repository.RankDao;

import java.time.LocalDate;
import java.util.List;

@Service
public class RankService {
    private final RankDao rankDao;

    public RankService(final RankDao rankDao) {
        this.rankDao = rankDao;
    }

    public List<ThemeResponse> getPopularThemeList(final LocalDate startDate, final LocalDate endDate, final Long count) {
        final List<Theme> themes = rankDao.findByDate(startDate, endDate, count);
        return themes.stream().map(ThemeResponse::from).toList();
    }
}
