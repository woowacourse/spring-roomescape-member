package roomescape.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.domain.Theme;
import roomescape.repository.ThemeDao;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ThemeQueryService {
    private final ThemeDao themeDao;

    public List<Theme> findAllThemes() {

        return themeDao.findAllThemes();
    }
}
