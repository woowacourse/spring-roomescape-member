package roomescape.service;

import roomescape.model.Theme;
import roomescape.repository.dao.ThemeDao;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicLong;

public class FakeThemeDao implements ThemeDao {

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
    public Theme saveTheme(Theme theme) {
        Theme newTheme = new Theme(index.getAndIncrement(), theme.getName(), theme.getDescription(), theme.getThumbnail());
        themes.add(newTheme);
        return newTheme;
    }

    @Override
    public void deleteThemeById(long id) {
        Theme targetTheme = themes.stream()
                .filter(theme -> theme.getId() == id)
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 테마입니다."));
        themes.remove(targetTheme);
    }

    @Override
    public Theme findThemeById(long id) {
        return themes.stream()
                .filter(theme -> theme.getId() == id)
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 테마입니다."));
    }

    @Override
    public List<Theme> findThemeRankingByDate(LocalDate before, LocalDate after, int limit) {
        return List.of(
                new Theme(10, "name10", "description10", "thumbnail10"),
                new Theme(9, "name9", "description9", "thumbnail9"),
                new Theme(1, "name1", "description1", "thumbnail1"),
                new Theme(2, "name2", "description2", "thumbnail2"),
                new Theme(3, "name3", "description3", "thumbnail3"),
                new Theme(4, "name4", "description4", "thumbnail4"),
                new Theme(5, "name5", "description5", "thumbnail5"),
                new Theme(6, "name6", "description6", "thumbnail6"),
                new Theme(7, "name7", "description7", "thumbnail7"),
                new Theme(8, "name8", "description8", "thumbnail8")
        );
    }
}
