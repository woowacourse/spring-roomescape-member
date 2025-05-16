package roomescape.reservation.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.reservation.domain.Theme;
import roomescape.util.repository.TestDataSourceFactory;

class JdbcThemeDaoTest {

    private JdbcThemeDao jdbcThemeDao;
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setup() {
        DataSource dataSource = TestDataSourceFactory.getEmbeddedDataSource();
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcThemeDao = new JdbcThemeDao(jdbcTemplate);
    }

    @AfterEach
    void dropTable() {
        String dropSql = "DROP TABLE IF EXISTS reservation, reservation_time, theme, member";
        jdbcTemplate.execute(dropSql);
    }

    @DisplayName("테마 데이터를 저장한다")
    @Test
    void save_test() {
        // given
        String name = "레벨1 탈출";
        String description = "우테코 레벨1를 탈출하는 내용입니다.";
        String thumbnail = "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg";
        Theme theme = new Theme(null, name, description, thumbnail);

        // when
        Long savedId = jdbcThemeDao.saveAndReturnId(theme);

        // then
        Theme savedTheme = jdbcThemeDao.findById(savedId).get();
        assertAll(
                () -> assertThat(savedTheme.getId()).isEqualTo(savedId),
                () -> assertThat(savedTheme.getName()).isEqualTo(name),
                () -> assertThat(savedTheme.getDescription()).isEqualTo(description),
                () -> assertThat(savedTheme.getThumbnail()).isEqualTo(thumbnail)
        );

    }

    @DisplayName("테마를 조회한다")
    @Test
    void find_all_test() {
        // when
        List<Theme> themes = jdbcThemeDao.findAll();

        // then
        assertAll(
                () -> assertThat(themes).hasSize(6),
                () -> assertThat(themes).extracting(Theme::getDescription)
                        .containsExactlyInAnyOrder(
                                "우테코 레벨1를 탈출하는 내용입니다.",
                                "우테코 레벨2를 탈출하는 내용입니다.",
                                "우테코 레벨3를 탈출하는 내용입니다.",
                                "우테코 레벨4를 탈출하는 내용입니다.",
                                "우테코 레벨5를 탈출하는 내용입니다.",
                                "우테코 레벨6를 탈출하는 내용입니다."
                        )
        );

    }

    @DisplayName("테마를 삭제한다")
    @Test
    void delete_test() {
        // given
        Long deleteId = 6L;

        // when
        jdbcThemeDao.deleteById(deleteId);

        // then
        assertAll(
                () -> assertThat(jdbcThemeDao.findAll()).hasSize(5),
                () -> assertThat(jdbcThemeDao.findById(6L).isEmpty()).isTrue()
        );

    }

    @DisplayName("특정 ID의 테마를 조회한다")
    @Test
    void find_by_id_test() {
        // given
        Long id = 3L;

        // when & then
        Theme theme = jdbcThemeDao.findById(id).get();
        assertAll(
                () -> assertThat(theme.getId()).isEqualTo(3L),
                () -> assertThat(theme.getName()).isEqualTo("레벨3 탈출"),
                () -> assertThat(theme.getDescription()).isEqualTo("우테코 레벨3를 탈출하는 내용입니다."),
                () -> assertThat(theme.getThumbnail()).isEqualTo(
                        "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg")
        );

    }

    @DisplayName("인기 테마 목록의 ID를 조회한다")
    @Test
    void find_top_theme_id_by_date_range_test() {
        // given
        LocalDate start = LocalDate.now().minusDays(3);
        LocalDate end = LocalDate.now().plusDays(3);
        int limit = 10;

        // when
        List<Long> actual = jdbcThemeDao.findTopThemeIdByDateRange(start, end, limit);

        // then
        assertThat(actual).containsExactly(3L, 4L, 2L, 5L);
    }

    @DisplayName("테마 ID 목록을 이용하여 테마들을 조회한다")
    @Test
    void find_by_id_in_test() {
        // given
        List<Long> ids = List.of(2L, 5L);

        // when
        List<Theme> themes = jdbcThemeDao.findByIdIn(ids);

        // then
        assertAll(
                () -> assertThat(themes).extracting(Theme::getDescription)
                        .containsExactlyInAnyOrder(
                                "우테코 레벨2를 탈출하는 내용입니다.",
                                "우테코 레벨5를 탈출하는 내용입니다."
                        ),
                () -> assertThat(themes).extracting(Theme::getId)
                        .containsExactlyInAnyOrder(2L, 5L)
        );
    }

    @DisplayName("조회하려는 데이터가 없으면 빈 배열을 반환한다")
    @Test
    void find_by_id_in_empty_test() {
        // given
        List<Long> ids = new ArrayList<>();

        // when
        List<Theme> findThemes = jdbcThemeDao.findByIdIn(ids);

        // then
        assertThat(findThemes).isEmpty();
    }

}
