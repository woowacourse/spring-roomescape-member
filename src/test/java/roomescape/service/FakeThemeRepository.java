package roomescape.service;

import roomescape.model.Theme;
import roomescape.repository.ThemeRepository;

import java.util.ArrayList;
import java.util.List;

public class FakeThemeRepository implements ThemeRepository {

    private List<Theme> themes = new ArrayList<>(List.of(
            new Theme(1, "에버", "공포", "공포.jpg"),
            new Theme(2, "배키", "미스터리", "미스터리.jpg"),
            new Theme(3, "포비", "스릴러", "스릴러.jpg")
    ));

    @Override
    public List<Theme> findAllThemes() {
        return themes;
    }

    @Override
    public Theme addTheme(Theme theme) {
        return null;
    }

    @Override
    public long deleteTheme(long id) {
        return 0;
    }
}
