package roomescape.service;

import roomescape.model.Theme;
import roomescape.repository.ThemeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicLong;

public class FakeThemeRepository implements ThemeRepository {

    private List<Theme> themes = new ArrayList<>(List.of(
            new Theme(1, "에버", "공포", "공포.jpg"),
            new Theme(2, "배키", "미스터리", "미스터리.jpg"),
            new Theme(3, "포비", "스릴러", "스릴러.jpg")
    ));

    private AtomicLong index = new AtomicLong(1L);

    @Override
    public List<Theme> findAllThemes() {
        return themes;
    }

    @Override
    public Theme addTheme(Theme theme) {
        Theme newTheme = new Theme(index.getAndIncrement(), theme.getName(), theme.getDescription(), theme.getThumbnail());
        themes.add(newTheme);
        return newTheme;
    }

    @Override
    public void deleteTheme(long id) {
        Theme targetTheme = themes.stream()
                .filter(theme -> theme.getId() == id)
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 테마입니다."));
        themes.remove(targetTheme);
    }
}
