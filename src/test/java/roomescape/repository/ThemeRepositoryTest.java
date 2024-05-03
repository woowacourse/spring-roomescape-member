package roomescape.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import roomescape.domain.Theme;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Sql(scripts = {"/drop.sql", "/schema.sql", "/data.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Import(H2ThemeRepository.class)
@JdbcTest
class ThemeRepositoryTest {

    final long LAST_ID = 4;
    final Theme exampleFirstTheme = new Theme(
            1L,
            "Spring",
            "A time of renewal and growth, as nature awakens from its slumber and bursts forth with vibrant colors and fragrant blooms.",
            "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
    );

    @Autowired
    ThemeRepository themeRepository;

    @Test
    @DisplayName("모든 테마 목록을 조회한다.")
    void findAll() {
        // given & when
        final List<Theme> actual = themeRepository.findAll();

        // then
        assertThat(actual).hasSize((int) LAST_ID);
        assertThat(actual.get(0)).isEqualTo(exampleFirstTheme);
    }

    @Test
    @DisplayName("특정 id를 통해 테마를 조회한다.")
    void findByIdPresent() {
        // given & when
        final Optional<Theme> actual = themeRepository.findById(exampleFirstTheme.getId());

        // then
        assertThat(actual).hasValue(exampleFirstTheme);
    }

    @Test
    @DisplayName("존재하지 않는 테마를 조회할 경우 빈 값을 반환한다.")
    void findByIdNotPresent() {
        // given & when
        final Optional<Theme> actual = themeRepository.findById(LAST_ID + 1);

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    @DisplayName("테마를 저장하면 새로운 아이디가 부여된다.")
    void save() {
        // given
        final Theme theme = new Theme(null, "ThemeName", "ThemeDescription", "ThemeImage");

        // when
        final Theme actual = themeRepository.save(theme);

        // then
        assertThat(actual.getId()).isPositive();
    }

    @Test
    @DisplayName("등록된 테마 번호로 삭제한다.")
    void deleteByIdPresent() {
        // given
        final Long id = LAST_ID;

        // when & then
        assertThat(themeRepository.findById(id)).isPresent();
        assertThat(themeRepository.delete(id)).isNotZero();
        assertThat(themeRepository.findById(id)).isEmpty();
    }

    @Test
    @DisplayName("존재하지 않는 테마를 삭제할 경우 아무런 영향이 없다.")
    void deleteByNotPresent() {
        // given
        final Long id = LAST_ID + 1;

        // when & then
        assertThat(themeRepository.findById(id)).isEmpty();
        assertThat(themeRepository.delete(id)).isZero();
    }
}
