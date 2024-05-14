package roomescape.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import roomescape.domain.Theme;

@JdbcTest
@Sql(scripts = "/reset_test_data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_CLASS)
public class ThemeRepositoryTest {

    private ThemeRepository themeRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        themeRepository = new ThemeRepository(jdbcTemplate);
    }

    @Test
    @DisplayName("모든 테마를 조회한다.")
    void findAll() {
        // when
        List<Theme> themes = themeRepository.findAll();

        // then
        assertThat(themes).hasSize(2);
    }

    @Test
    @DisplayName("테마를 생성한다.")
    void create() {
        // given
        Theme theme = new Theme("레벨4 탈출",
                "우테코 레벨4를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");

        // when
        themeRepository.create(theme);

        // then
        List<Theme> themes = themeRepository.findAll();
        assertThat(themes).hasSize(3);
    }

    @Test
    @DisplayName("테마를 삭제한다.")
    void delete() {
        // given
        Long id = 2L;

        // when
        themeRepository.removeById(id);

        // then
        List<Theme> themes = themeRepository.findAll();
        assertThat(themes).hasSize(1);
    }

    // 테스트 데이터 - 테마아이디(해당테마 예약 갯수): 6(6), 5(5), 7(5), 4(4), 3(3), 1(1), 8(1), 2(0), 9(0), 10(0)
    @Test
    @Sql(scripts = {"/reset_test_data.sql", "/popular_themes_data.sql"},
            executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    @DisplayName("최근 일주일간 인기 테마를 조회할 수 있다.")
    void findPopularThemes() {
        // given
        LocalDate startDate = LocalDate.parse("2024-04-01");
        LocalDate endDate = LocalDate.parse("2024-04-07");
        int limit = 10;

        // when
        List<Theme> popularThemes = themeRepository.findOrderedByReservationCountInPeriod(startDate, endDate,
                limit);

        // then
        assertThat(popularThemes)
                .hasSize(10)
                .extracting("id")
                .containsExactly(6L, 5L, 7L, 4L, 3L, 1L, 8L, 2L, 9L, 10L);
    }
}
