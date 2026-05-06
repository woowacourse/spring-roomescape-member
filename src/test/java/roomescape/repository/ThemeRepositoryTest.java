package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import roomescape.domain.Theme;
import roomescape.test.util.TestDatabaseUtils;

@JdbcTest
class ThemeRepositoryTest {

    private static final long DEFAULT_ID = 5;
    private static final long NOT_EXIST_ID = 999;
    private static final String DEFAULT_NAME = "name";
    private static final String DEFAULT_DESCRIPTION = "description";
    private static final String DEFAULT_IMAGE_URL = "imageUrl";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private ThemeRepository themeRepository;

    @BeforeEach
    void setUp() {
        TestDatabaseUtils.clearTables(jdbcTemplate);
        this.themeRepository = new ThemeRepository(jdbcTemplate);
    }

    @Nested
    class 테마를_저장한다 {
        @Test
        void 새로운_테마를_저장한다() {
            // given
            Theme theme = Theme.create(
                    DEFAULT_NAME,
                    DEFAULT_DESCRIPTION,
                    DEFAULT_IMAGE_URL
            );

            // when
            themeRepository.persist(theme);

            // then
            String themeCountSql = "SELECT count(*)"
                    + " FROM theme";
            Integer themeCount = jdbcTemplate.queryForObject(
                    themeCountSql,
                    Integer.class
            );

            assertThat(themeCount).isEqualTo(1);
        }

        @Test
        void 저장한_테마를_반환한다() {
            // given
            Theme transientTheme = Theme.create(
                    DEFAULT_NAME,
                    DEFAULT_DESCRIPTION,
                    DEFAULT_IMAGE_URL
            );

            // when
            Theme persistedTheme = themeRepository.persist(transientTheme);

            // then
            String selectSql = "SELECT id, name, description, image_url"
                    + " FROM theme";
            List<Theme> foundThemes = jdbcTemplate.query(selectSql, themeRowMapper());

            assertThat(foundThemes).hasSize(1);
            assertThat(foundThemes.getFirst()).isEqualTo(persistedTheme);
        }
    }

    @Test
    void 저장된_모든_테마를_조회한다() {
        // given
        int insertCount = 5;
        insertThemeAsAmount(insertCount);

        // when
        List<Theme> themes = themeRepository.findAll();

        // then
        assertThat(themes).hasSize(insertCount);
    }

    @Nested
    class 테마를_ID_기준으로_조회한다 {

        @Test
        void 테마를_ID_기준으로_조회한다() {
            // given
            long id = DEFAULT_ID;
            String name = DEFAULT_NAME;
            String description = DEFAULT_DESCRIPTION;
            String imageUrl = DEFAULT_IMAGE_URL;

            insertTheme(id, name, description, imageUrl);
            Theme expected = Theme.retrieve(id, name, description, imageUrl);

            // when
            Optional<Theme> actual = themeRepository.findById(id);

            // then
            assertThat(actual).hasValue(expected);
        }

        @Test
        void ID로_레코드가_조회되지_않는다면_빈_Optional을_반환한다() {
            // when
            Optional<Theme> theme = themeRepository.findById(NOT_EXIST_ID);

            // then
            assertThat(theme).isEmpty();
        }
    }

    @Nested
    class 테마를_제거한다 {

        @Test
        void ID_기반으로_테마를_제거한다() {
            // given
            long id = DEFAULT_ID;
            insertTheme(
                    id,
                    DEFAULT_NAME,
                    DEFAULT_DESCRIPTION,
                    DEFAULT_IMAGE_URL
            );

            // when
            themeRepository.delete(id);

            // then
            String countSql = "SELECT count(*)"
                    + " FROM theme"
                    + " WHERE id = ?";
            Integer themeCount = jdbcTemplate.queryForObject(
                    countSql,
                    Integer.class,
                    id
            );

            assertThat(themeCount).isEqualTo(0);
        }

        @Test
        void 레코드가_제거됐다면_true를_반환한다() {
            // given
            long id = DEFAULT_ID;
            insertTheme(
                    id,
                    DEFAULT_NAME,
                    DEFAULT_DESCRIPTION,
                    DEFAULT_IMAGE_URL
            );

            // when
            boolean deleted = themeRepository.delete(id);

            // then
            assertThat(deleted).isTrue();
        }

        @Test
        void 아무_레코드도_제거되지_않았다면_false를_반환한다() {
            boolean deleted = themeRepository.delete(NOT_EXIST_ID);

            assertThat(deleted).isFalse();
        }
    }

    private void insertTheme(
            long id,
            String name,
            String description,
            String imageUrl
    ) {
        String insertSql = "INSERT INTO theme(id, name, description, image_url)"
                + " VALUES (?, ?, ?, ?)";

        jdbcTemplate.update(
                insertSql,
                id,
                name,
                description,
                imageUrl
        );
    }

    private void insertThemeAsAmount(int count) {
        for (int i = 0; i < count; i++) {
            String insertSql = "INSERT INTO theme(name, description, image_url)"
                    + " VALUES (?, ? ,?)";

            jdbcTemplate.update(
                    insertSql,
                    DEFAULT_NAME,
                    DEFAULT_DESCRIPTION,
                    DEFAULT_IMAGE_URL
            );
        }
    }

    private RowMapper<Theme> themeRowMapper() {
        return (resultSet, rowNum) -> {
            long id = resultSet.getLong("id");
            String name = resultSet.getString("name");
            String description = resultSet.getString("description");
            String imageUrl = resultSet.getString("image_url");

            return Theme.retrieve(id, name, description, imageUrl);
        };
    }
}
