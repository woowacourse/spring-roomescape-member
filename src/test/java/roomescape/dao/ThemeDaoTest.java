package roomescape.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import roomescape.dto.PopularThemeProjection;

@JdbcTest
@ActiveProfiles("test")
@Import(ThemeDao.class)
public class ThemeDaoTest {

    @Autowired
    private ThemeDao themeDao;

    @Test
    @Sql("/popular-themes.sql")
    void 최근_1주동안_예약이_많았던_테마_상위_10개를_조회한다() {
        List<PopularThemeProjection> popularThemeProjections = themeDao.findPopularThemes(
                LocalDate.of(2026, 5, 1), LocalDate.of(2026, 5, 7));

        assertThat(popularThemeProjections).hasSize(10);
        assertThat(popularThemeProjections)
                .extracting(PopularThemeProjection::name)
                .containsExactly(
                        "이든의 공포 하우스",
                        "정콩이의 방탈출",
                        "우주 정거장 탈출",
                        "고대 유적의 비밀",
                        "마법사의 서재",
                        "좀비 연구소",
                        "해적선의 보물",
                        "미스터리 호텔",
                        "시간의 문",
                        "사라진 탐정"
                );
        assertThat(popularThemeProjections)
                .extracting(PopularThemeProjection::rank)
                .containsExactly(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L);
    }
}
