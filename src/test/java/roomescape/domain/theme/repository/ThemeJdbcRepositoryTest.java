package roomescape.domain.theme.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import roomescape.domain.theme.entity.Theme;
import roomescape.domain.theme.response.PopularThemeResponse;
import roomescape.domain.theme.response.ThemeReservationTimeResponse;

@JdbcTest
class ThemeJdbcRepositoryTest {

    @Autowired
    DataSource dataSource;

    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    SimpleJdbcInsert simpleJdbcInsert;

    ThemeRepository themeRepository;

    @BeforeEach
    void setUp() {
        themeRepository = new ThemeJdbcRepository(jdbcTemplate, dataSource);

        simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("theme")
                .usingGeneratedKeyColumns("id");
    }

    @Test
    @DisplayName("모든 테마를 조회한다.")
    void findAllTest() {
        // when
        List<Theme> themes = themeRepository.findAll();

        // then
        assertThat(themes).hasSize(10);
        assertThat(themes)
                .extracting(Theme::getName)
                .contains(
                        "워너비",
                        "공포의 지하실",
                        "우주 정거장",
                        "탐정 사무소",
                        "마법 학교",
                        "박물관이 살아있다",
                        "해적선",
                        "미래 도시",
                        "공룡 시대",
                        "비밀의 정원"
                );
    }

    @Test
    @DisplayName("ID로 테마를 조회한다.")
    void findByIdTest() {
        // given
        long themeId = 1L;

        // when
        Optional<Theme> found = themeRepository.findById(themeId);

        // then
        assertThat(found).isPresent();

        Theme theme = found.get();
        assertThat(theme.getId()).isEqualTo(themeId);
        assertThat(theme.getName()).isEqualTo("워너비");
    }

    @Test
    @DisplayName("특정 날짜의 테마별 예약 가능 시간을 조회한다.")
    void findAllThemeReservationTimesByThemeIdAndDateTest() {
        // given
        long themeId = 1L;
        LocalDate date = LocalDate.of(2026, 5, 6);

        // when
        List<ThemeReservationTimeResponse> result = themeRepository
                .findAllThemeReservationTimesByThemeIdAndDate(themeId, date);

        // then
        assertThat(result).hasSize(6);
        assertThat(result)
                .extracting(
                        ThemeReservationTimeResponse::id,
                        ThemeReservationTimeResponse::startAt,
                        ThemeReservationTimeResponse::isAvailable
                )
                .containsExactly(
                        tuple(1L, LocalTime.of(10, 0), false),
                        tuple(2L, LocalTime.of(11, 0), true),
                        tuple(3L, LocalTime.of(12, 0), false),
                        tuple(4L, LocalTime.of(13, 0), true),
                        tuple(5L, LocalTime.of(14, 0), false),
                        tuple(6L, LocalTime.of(15, 0), true)
                );
    }

    @Test
    @DisplayName("지정한 기간 동안 가장 인기가 많은 테마 상위 10개를 조회한다.")
    void findPopularThemesTest() {
        // given
        LocalDate startDate = LocalDate.of(2026, 4, 29);
        LocalDate endDate = LocalDate.of(2026, 5, 5);
        int limit = 10;

        // when
        List<PopularThemeResponse> result = themeRepository.findPopularThemes(
                startDate,
                endDate,
                limit
        );

        // then
        assertThat(result).hasSize(10);
        assertThat(result)
                .extracting("name", "rank")
                .containsSubsequence(
                        tuple("워너비", 1),
                        tuple("공포의 지하실", 2)
                );
    }

    @Test
    @DisplayName("테마를 저장한다.")
    void saveTest() {
        // given
        Theme theme = new Theme("new name", "new description", "new url");

        // when
        Theme saved = themeRepository.save(theme);

        // then
        assertThat(saved.getId()).isNotNull();

        Theme found = findThemeById(saved.getId());
        assertThat(found.getId()).isEqualTo(saved.getId());
        assertThat(found.getName()).isEqualTo("new name");
        assertThat(found.getDescription()).isEqualTo("new description");
        assertThat(found.getThumbnailUrl()).isEqualTo("new url");
    }

    @Test
    @DisplayName("ID로 테마를 삭제한다.")
    void deleteByIdTest() {
        // given
        long generatedId = insertTheme("to be deleted", "description", "url");

        // when
        themeRepository.deleteById(generatedId);

        // then
        List<Theme> themes = findThemesById(generatedId);
        assertThat(themes).isEmpty();
    }

    private long insertTheme(String name, String description, String thumbnailUrl) {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("name", name)
                .addValue("description", description)
                .addValue("thumbnail_url", thumbnailUrl);

        return simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
    }

    private Theme findThemeById(Long id) {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("id", id);

        return jdbcTemplate.queryForObject(
                "SELECT * FROM theme WHERE id = :id",
                parameters,
                themeRowMapper()
        );
    }

    private List<Theme> findThemesById(Long id) {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("id", id);

        return jdbcTemplate.query(
                "SELECT * FROM theme WHERE id = :id",
                parameters,
                themeRowMapper()
        );
    }

    private RowMapper<Theme> themeRowMapper() {
        return (resultSet, rowNumber) -> new Theme(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getString("thumbnail_url")
        );
    }
}
