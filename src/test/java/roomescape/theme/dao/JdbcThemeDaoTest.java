package roomescape.theme.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.theme.domain.Theme;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class JdbcThemeDaoTest {

    @Autowired
    private ThemeDao themeDAO;

    @DisplayName("데이터 베이스에 방 테마를 추가하고 id 값을 반환한다")
    @Test
    void insertTest() {
        // given
        final Theme roomTheme = new Theme("공포", "공포 테마 입니다", "url");

        // when
        final long result = themeDAO.insert(roomTheme);

        // then
        assertThat(result).isEqualTo(4L);
    }

    @DisplayName("같은 이름의 테마가 존재하면 true를 반환한다")
    @Test
    void existsByNameTest() {
        // given // when
        final boolean result = themeDAO.existsByName("예시 1");

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("존재하는 모든 방 테마를 찾아 반환한다")
    @Test
    void findAllTest() {
        // given // when
        final List<Theme> result = themeDAO.findAll();

        // then
        Assertions.assertNotNull(result);
        assertAll(
                () -> assertThat(result).hasSize(3),
                () -> assertThat(result.getFirst().getName()).isEqualTo("예시 1"),
                () -> assertThat(result.get(1).getName()).isEqualTo("예시 2"),
                () -> assertThat(result.getLast().getName()).isEqualTo("예시 3")
        );
    }

    @DisplayName("id에 해당하는 방 테마를 반환한다")
    @Test
    void findByIdTest() {
        // given
        final long id = 1L;

        // when
        final Optional<Theme> resultOptional = themeDAO.findById(id);

        // then
        assertThat(resultOptional).isPresent();
        assertThat(resultOptional.get().getName()).isEqualTo("예시 1");
    }

    @DisplayName("예약 횟수에 따라 내림 차순으로 방 테마를 반환한다")
    @Test
    void findPopularThemesTest() {
        // given // when
        List<Theme> popularThemes = themeDAO.findPopularThemes(
                LocalDate.of(2024, 10, 1), LocalDate.of(2025, 5, 1), 10);

        // then
        Assertions.assertNotNull(popularThemes);
        assertAll(
                () -> assertThat(popularThemes).hasSize(2),
                () -> assertThat(popularThemes.getFirst().getId()).isEqualTo(1L),
                () -> assertThat(popularThemes.getFirst().getName()).isEqualTo("예시 1"),
                () -> assertThat(popularThemes.getLast().getId()).isEqualTo(2L),
                () -> assertThat(popularThemes.getLast().getName()).isEqualTo("예시 2")
        );
    }

    @DisplayName("주어진 id에 해당하는 방 테마를 삭제한다")
    @Test
    void deleteByIdTest() {
        // given
        final long id = 3L;

        // when
        final boolean result = themeDAO.deleteById(id);

        // then
        assertThat(result).isTrue();
    }
}
