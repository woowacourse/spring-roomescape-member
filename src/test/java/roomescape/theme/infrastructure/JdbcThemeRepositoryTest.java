package roomescape.theme.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static roomescape.testFixture.Fixture.THEME_1;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import roomescape.testFixture.JdbcHelper;
import roomescape.theme.domain.Theme;

@JdbcTest
@Import(JdbcThemeRepository.class)
@ActiveProfiles("test")
class JdbcThemeRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private JdbcThemeRepository jdbcThemeRepository;

    @BeforeEach
    void cleanDatabase() {
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");
        jdbcTemplate.execute("TRUNCATE TABLE reservation");
        jdbcTemplate.execute("ALTER TABLE reservation ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("TRUNCATE TABLE reservation_time");
        jdbcTemplate.execute("ALTER TABLE reservation_time ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("TRUNCATE TABLE theme");
        jdbcTemplate.execute("ALTER TABLE theme ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
    }

    @DisplayName("모든 테마를 조회할 수 있다.")
    @Test
    void findAll() {
        // given
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES ('테마1', '테마 1입니다.', '썸네일입니다.')");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES ('테마2', '테마 2입니다.', '썸네일입니다.')");

        // when
        List<Theme> themes = jdbcThemeRepository.findAll();

        // then
        assertThat(themes).hasSize(2);
    }

    @DisplayName("테마 생성 후 id를 반환한다")
    @Test
    void save() {
        //given
        Theme themeWithoutId = Theme.createNew("테마1", "테마 1입니다.", "썸네일");

        //when
        Long id = jdbcThemeRepository.save(themeWithoutId);

        //then
        assertThat(id).isEqualTo(1);
    }

    @DisplayName("id로 해당하는 테마를 삭제한다")
    @Test
    void delete() {
        //given
        Long themeId = 1L;
        jdbcTemplate.update("INSERT INTO theme (id, name, description, thumbnail) VALUES (?, ?, ?, ?)",
                themeId, "테마1", "테마 1입니다.", "썸네일입니다.");
        assertThat(getThemesCount()).isEqualTo(1);

        //when
        jdbcThemeRepository.deleteById(themeId);

        //then
        assertThat(getThemesCount()).isEqualTo(0);
    }

    @DisplayName("존재하지 않는 ID로 테마 삭제 시도시 예외가 발생하지 않는다.")
    @Test
    void deleteNonExistingTheme_notThrowException() {
        // given
        Long notExistingId = 999L;

        // when & then
        assertDoesNotThrow(() -> jdbcThemeRepository.deleteById(notExistingId));
    }

    @DisplayName("ID로 시간을 조회할 수 있다")
    @Test
    void findById() {
        //given
        JdbcHelper.insertTheme(jdbcTemplate, THEME_1);
        //when
        Long id = THEME_1.getId();
        Optional<Theme> result = jdbcThemeRepository.findById(id);

        //then
        assertThat(result).isPresent();
        Theme resultTheme = result.get();
        assertAll(
                () -> assertThat(resultTheme.getId()).isEqualTo(id),
                () -> assertThat(resultTheme.getName()).isEqualTo(THEME_1.getName())
        );
    }

    @DisplayName("ID로 조회했을 때 존재하지 않으면 빈 Optional을 반환한다.")
    @Test
    void findMemberById_emptyOptional() {
        // given
        Long nonExistingId = 999L;

        // when
        Optional<Theme> result = jdbcThemeRepository.findById(nonExistingId);

        // then
        assertThat(result).isEmpty();
    }

    private int getThemesCount() {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM theme", Integer.class);
    }
}
