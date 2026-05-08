package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import roomescape.domain.Theme;
import roomescape.test.util.TestDatabaseUtils;

@JdbcTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ThemeRepositoryTest {

    private static boolean persistTestSuccessful = false;
    private static boolean findAllTestSuccessful = false;

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

    @Order(1)
    @Test
    void 새로운_테마를_저장하고_저장된_테마를_반환한다() {
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

        persistTestSuccessful = true;
    }

    @Order(2)
    @Test
    void 저장된_모든_테마를_조회한다() {
        skipIfPersistTestFailed();

        // given
        Theme transientTheme = Theme.create(
                DEFAULT_NAME,
                DEFAULT_DESCRIPTION,
                DEFAULT_IMAGE_URL
        );
        Theme persistedTheme = themeRepository.persist(transientTheme);

        // when
        List<Theme> foundThemes = themeRepository.findAll();

        // then
        assertThat(foundThemes).containsExactly(persistedTheme);

        findAllTestSuccessful = true;
    }

    @Nested
    class 저장_조회_의존_테스트 {

        @BeforeEach
        void skipIfDependentTestFailed() {
            skipIfPersistTestFailed();
            skipIfFindAllTestFailed();
        }

        @Nested
        class 테마를_ID_기준으로_조회한다 {

            @Test
            void 테마를_ID_기준으로_조회한다() {
                // given
                Theme transientTheme = Theme.create(
                        DEFAULT_NAME,
                        DEFAULT_DESCRIPTION,
                        DEFAULT_IMAGE_URL
                );
                Theme persistedTheme = themeRepository.persist(transientTheme);

                // when
                Optional<Theme> foundTheme = themeRepository.findById(persistedTheme.getId());

                // then
                assertThat(foundTheme).hasValue(persistedTheme);
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
                Theme transientTheme = Theme.create(
                        DEFAULT_NAME,
                        DEFAULT_DESCRIPTION,
                        DEFAULT_IMAGE_URL
                );
                Theme persistedTheme = themeRepository.persist(transientTheme);

                // when
                themeRepository.delete(persistedTheme.getId());

                // then
                List<Theme> foundThemes = themeRepository.findAll();

                assertThat(foundThemes).doesNotContain(persistedTheme);
            }

            @Test
            void 레코드가_제거됐다면_true를_반환한다() {
                // given
                Theme transientTheme = Theme.create(
                        DEFAULT_NAME,
                        DEFAULT_DESCRIPTION,
                        DEFAULT_IMAGE_URL
                );
                Theme persistedTheme = themeRepository.persist(transientTheme);

                // when
                boolean deleted = themeRepository.delete(persistedTheme.getId());

                // then
                assertThat(deleted).isTrue();
            }

            @Test
            void 아무_레코드도_제거되지_않았다면_false를_반환한다() {
                boolean deleted = themeRepository.delete(NOT_EXIST_ID);

                assertThat(deleted).isFalse();
            }
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

    private static void skipIfPersistTestFailed() {
        Assumptions.assumeTrue(persistTestSuccessful, "저장 기능 테스트에 실패하여 다른 테스트를 수행하지 않습니다.");
    }

    private static void skipIfFindAllTestFailed() {
        Assumptions.assumeTrue(findAllTestSuccessful, "조회 기능 테스트에 실패하여 다른 테스트를 수행하지 않습니다.");
    }
}
