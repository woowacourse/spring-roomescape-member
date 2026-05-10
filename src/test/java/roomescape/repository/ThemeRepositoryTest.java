package roomescape.repository;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.dto.ThemeRequest;
import roomescape.model.Theme;


@JdbcTest
public class ThemeRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private ThemeRepository themeRepository;

    @BeforeEach
    public void setUp() {
        themeRepository = new ThemeRepository(jdbcTemplate);
    }

    @Test
    public void 새로운_테마를_등록할_수_있다() {
        // given
        ThemeRequest themeRequest = new ThemeRequest("테스트 테마", "테스트 테마입니다", "url.com");

        // when
        Theme newTheme = themeRepository.save(themeRequest.name(), themeRequest.description(), themeRequest.url());

        // then
        Assertions.assertEquals(18L, newTheme.getId());
        Assertions.assertEquals(themeRequest.name(), newTheme.getName());
        Assertions.assertEquals(themeRequest.description(), newTheme.getDescription());
        Assertions.assertEquals(themeRequest.url(), newTheme.getUrl());
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
        Long themeId = 2L;

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
        Theme theme = themeRepository.findById(themeId).get();

        // then
        Assertions.assertEquals(themeId, theme.getId());
        Assertions.assertEquals("루팡의 예고장", theme.getName());
        Assertions.assertEquals("세계 최고의 보석 '여신의 눈물'이 전시된 박물관. 당신은 예고된 시간에 맞춰 보석을 훔쳐낼 수 있을까요?", theme.getDescription());
        Assertions.assertEquals("https://escape.com/images/lupin.png", theme.getUrl());

    }

    @Test
    public void 지난_일주일간_가장_예약이_많았던_상위_10개_테마를_가져온다() {
        // given
        String currentDate = "2026-05-05";
        String lastWeekDate = "2026-04-28";
        int limit = 10;

        // when
        List<Theme> themes = themeRepository.findByCurrentDateAndLastWeekDateAndLimit(currentDate, lastWeekDate, limit);

        // then
        Assertions.assertEquals(5, themes.get(0).getId());
        Assertions.assertEquals(2, themes.get(1).getId());
        Assertions.assertEquals(1, themes.get(2).getId());
        Assertions.assertEquals(3, themes.get(3).getId());
        Assertions.assertEquals(4, themes.get(4).getId());
        Assertions.assertEquals(6, themes.get(5).getId());
    }
}
