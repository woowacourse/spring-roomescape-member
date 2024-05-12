package roomescape.infrastructure.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import roomescape.domain.Description;
import roomescape.domain.Theme;
import roomescape.domain.ThemeName;
import roomescape.support.IntegrationTestSupport;

class ThemeRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private ThemeRepository target;

    @Test
    @DisplayName("모든 테마 데이터를 조회한다.")
    void findAll() {
        List<Theme> themes = target.findAll();

        assertThat(themes).hasSize(2);
    }

    @Test
    @DisplayName("테마 데이터가 존재하지 않으면 빈 리스트를 반환한다.")
    void empty() {
        cleanUp("reservation");
        cleanUp("theme");

        List<Theme> themes = target.findAll();

        assertThat(themes).isEmpty();
    }

    @Test
    @DisplayName("특정 테마 id의 데이터를 조회한다.")
    void findById() {
        Optional<Theme> findReservationTime = target.findById(2L);

        assertThat(findReservationTime)
                .map(Theme::getName)
                .map(ThemeName::value)
                .isNotEmpty()
                .get()
                .isEqualTo("레벨2 탈출");
    }

    @Test
    @DisplayName("존재하지 않는 테마 id의 데이터를 조회한다.")
    void notFound() {
        Optional<Theme> findReservationTime = target.findById(5L);

        assertThat(findReservationTime).isEmpty();
    }

    @Test
    @DisplayName("새로운 테마 데이터를 생성한다.")
    void create() {
        Theme theme = new Theme(
                new ThemeName("레벨 1 방탈출"),
                new Description("description"), "woowahan.com"
        );

        target.save(theme);

        int countRow = countRow("theme");
        assertThat(countRow).isEqualTo(3);
    }

    @Test
    @DisplayName("특정 id를 가진 테마를 삭제한다.")
    void delete() {
        target.removeById(2L);

        int countRow = countRow("theme");
        assertThat(countRow).isEqualTo(1);
    }

    // 테스트 데이터 - 테마아이디(해당테마 예약 갯수): 6(6), 5(5), 7(5), 4(4), 3(3), 1(1), 8(1), 2(0), 9(0), 10(0)
    @Test
    @Sql(scripts = {"/reset_test_data.sql", "/popular_themes_data.sql"})
    @DisplayName("최근 일주일간 인기 테마를 조회할 수 있다.")
    void findPopularThemes() {
        LocalDate startDate = LocalDate.parse("2024-04-01");
        LocalDate endDate = LocalDate.parse("2024-04-07");
        int limit = 10;

        List<Theme> popularThemes = target.findPopularThemes(startDate, endDate, limit);

        assertThat(popularThemes)
                .hasSize(limit)
                .extracting(Theme::getId)
                .containsExactly(6L, 5L, 7L, 4L, 3L, 1L, 8L, 2L, 9L, 10L);
    }
}
