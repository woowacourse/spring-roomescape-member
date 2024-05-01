package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.ThemeDao;
import roomescape.domain.Theme;
import roomescape.exception.NotExistThemeException;
import roomescape.service.dto.input.ThemeInput;
import roomescape.service.dto.output.ThemeOutput;

@Service
public class ThemeService {

    ThemeDao themeDao;

    public ThemeService(ThemeDao themeDao) {
        this.themeDao = themeDao;
    }

    public ThemeOutput createTheme(ThemeInput input) {
        Theme theme = themeDao.create(Theme.of(null, input.name(), input.description(), input.thumbnail()));
        return ThemeOutput.toOutput(theme);
    }

    public List<ThemeOutput> getAllThemes() {
        List<Theme> themes = themeDao.getAll();
        return ThemeOutput.toOutputs(themes);
    }

    public void deleteTheme(long id) {
        themeDao.find(id)
                .orElseThrow(() -> new NotExistThemeException(String.format("%d는 없는 id 입니다.", id)));

        themeDao.delete(id);
    }
}
