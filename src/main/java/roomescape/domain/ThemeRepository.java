package roomescape.domain;

import java.util.List;

public interface ThemeRepository {

    List<Theme> findAll();

    Theme save(Theme theme);

    Theme findById(Long id);

    void deleteById(Long id);

    List<Theme> findTopThemesWithinDays(int day, int limit); //todo: 이름 자연스럽게 바꾸기
}
