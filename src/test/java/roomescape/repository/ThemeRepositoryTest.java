package roomescape.repository;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import roomescape.model.Theme;


@JdbcTest
@Import(ThemeRepository.class)
public class ThemeRepositoryTest {

    @Autowired
    private ThemeRepository themeRepository;

    @Test
    public void 새로운_테마를_등록할_수_있다() {
        Theme theme = new Theme(null, "테스트 테마", "테스트 테마입니다", "url.com");

        Theme newTheme = themeRepository.save(theme);

        // then
        assertAll(
                () -> assertEquals(18L, newTheme.id()),
                () -> assertEquals("테스트 테마", newTheme.name()),
                () -> assertEquals("테스트 테마입니다", newTheme.description()),
                () -> assertEquals("url.com", newTheme.url())
        );
    }

    @Test
    public void 저장된_테마를_모두_조회할_수_있다() {
        List<Theme> themes = themeRepository.findAll();
        int themeSize = 17;
        assertEquals(themeSize, themes.size());
    }

    @Test
    public void 저장된_테마를_삭제할_수_있다() {
        Long themeId = 7L;

        themeRepository.deleteById(themeId);

        assertEquals(themeRepository.findAll().size(), 16);
    }

    @Test
    public void 특정_테마_아이디를_통해서_테마를_조회할_수_있다() {
        Long themeId = 2L;

        Optional<Theme> theme = themeRepository.findById(themeId);

        assertAll(
                () -> assertEquals(themeId, theme.get().id()),
                () -> assertEquals("루팡의 예고장", theme.get().name()),
                () -> assertEquals("세계 최고의 보석 '여신의 눈물'이 전시된 박물관. 당신은 예고된 시간에 맞춰 보석을 훔쳐낼 수 있을까요?",
                        theme.get().description()),
                () -> assertEquals("https://picsum.photos/seed/lupin/400/300", theme.get().url())
        );
    }

    @Test
    public void 지난_일주일간_가장_예약이_많았던_상위_10개_테마를_가져온다() {
        LocalDate currentDate = LocalDate.now().minusDays(1);
        LocalDate lastWeekDate = LocalDate.now().minusDays(8);
        int limit = 10;

        List<Theme> themes = themeRepository.findByCurrentDateAndLastWeekDateAndLimit(currentDate.toString(),
                lastWeekDate.toString(), limit);

        assertAll(
                () -> assertEquals(themes.get(0).id(), 5),
                () -> assertEquals(themes.get(1).id(), 2),
                () -> assertEquals(themes.get(2).id(), 1),
                () -> assertEquals(themes.get(3).id(), 3),
                () -> assertEquals(themes.get(4).id(), 4),
                () -> assertEquals(themes.get(5).id(), 6)
        );
    }
}
