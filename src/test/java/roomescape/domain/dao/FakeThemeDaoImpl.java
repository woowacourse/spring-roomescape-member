package roomescape.domain.dao;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import roomescape.dao.ThemeDao;
import roomescape.domain.Theme;

@Repository
@Qualifier("fakeThemeDao")
public class FakeThemeDaoImpl implements ThemeDao {

    private final AtomicLong index = new AtomicLong(1);
    private final List<Theme> themes = new ArrayList<>();

    @Override
    public List<Theme> findAllTheme() {
        System.out.println("페이크 객체 테마 가져오기가 실행되었음");
        return Collections.unmodifiableList(themes);
    }

    @Override
    public long saveTheme(Theme theme) {
        theme.setId(index.getAndIncrement());
        themes.add(theme);
        return index.longValue();
    }

    @Override
    public void deleteTheme(Long id) {
        Theme theme = findById(id).
            orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약번호 입니다."));
        themes.remove(theme);
    }

    @Override
    public Optional<Theme> findById(Long id) {
        return themes.stream()
            .filter(theme -> theme.getId().equals(id))
            .findFirst();
    }

    @Override
    public List<Theme> findAllThemeOfRanks(LocalDate startDate, LocalDate currentDate) {
        return List.of();
    }
}
