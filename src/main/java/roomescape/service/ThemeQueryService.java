package roomescape.service;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.dto.response.ThemeResponse;
import roomescape.repository.ThemeDao;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ThemeQueryService {

    private final ThemeDao themeDao;

    public List<ThemeResponse> findAllThemes() {
        return themeDao.findAllThemes().stream()
                .map(ThemeResponse::from)
                .toList();
    }

    public List<ThemeResponse> findPopularThemesBy(LocalDate startAt, LocalDate endAt, int limit) {
        return themeDao.findSortedPopularThemesBy(startAt, endAt, limit).stream()
                .map(ThemeResponse::from)
                .toList() ;
    }
}
