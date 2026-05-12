package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
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
        // given
        Theme theme = new Theme(null, "테스트 테마", "테스트 테마입니다", "url.com");

        // when
        Theme newTheme = themeRepository.save(theme);

        // then
        Assertions.assertEquals(18L, newTheme.getId());
        Assertions.assertEquals("테스트 테마", newTheme.getName());
        Assertions.assertEquals("테스트 테마입니다", newTheme.getDescription());
        Assertions.assertEquals("url.com", newTheme.getUrl());
    }

    @Test
    public void 저장된_테마를_모두_조회할_수_있다() {
        // when
        List<Theme> themes = themeRepository.findAll();
        int themeSize = 17;
        // then
        Assertions.assertEquals(themeSize, themes.size());
    }

    @Test
    public void 저장된_테마를_삭제할_수_있다() {
        // given
        Long themeId = 7L;

        // when
        themeRepository.deleteById(themeId);

        // then
        Assertions.assertEquals(themeRepository.findAll().size(), 16);
    }

    @Test
    public void 특정_테마_아이디를_통해서_테마를_조회할_수_있다() {
        // given
        Long themeId = 2L;

        // when
        Optional<Theme> theme = themeRepository.findById(themeId);

        // then
        Assertions.assertEquals(themeId, theme.get().getId());
        Assertions.assertEquals("루팡의 예고장", theme.get().getName());
        Assertions.assertEquals("세계 최고의 보석 '여신의 눈물'이 전시된 박물관. 당신은 예고된 시간에 맞춰 보석을 훔쳐낼 수 있을까요?",
                theme.get().getDescription());
        Assertions.assertEquals("https://picsum.photos/seed/lupin/400/300", theme.get().getUrl());

    }

    @Test
    public void 지난_일주일간_가장_예약이_많았던_상위_10개_테마를_가져온다() {
        // given
        LocalDate currentDate = LocalDate.now().minusDays(1);
        LocalDate lastWeekDate = LocalDate.now().minusDays(8);
        int limit = 10;

        // when
        List<Theme> themes = themeRepository.findByCurrentDateAndLastWeekDateAndLimit(currentDate.toString(),
                lastWeekDate.toString(), limit);

        // then
        Assertions.assertEquals(themes.get(0).getId(), 5);
        Assertions.assertEquals(themes.get(1).getId(), 2);
        Assertions.assertEquals(themes.get(2).getId(), 1);
        Assertions.assertEquals(themes.get(3).getId(), 3);
        Assertions.assertEquals(themes.get(4).getId(), 4);
        Assertions.assertEquals(themes.get(5).getId(), 6);
    }
}
