package roomescape.theme.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import roomescape.fixture.Fixture;
import roomescape.theme.model.Theme;

@ActiveProfiles("test")
@SpringBootTest
@Sql(scripts = {"/delete-data.sql", "/init-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class JdbcThemeRepositoryTest {

    @Autowired
    private JdbcThemeRepository themeRepository;

    @Test
    @DisplayName("Theme 저장한 후 저장한 테마 값을 반환한다.")
    void save() {
        Theme theme = new Theme(null, "마크", "노력함", "https://asd.cmom");

        assertThat(themeRepository.save(theme))
                .isEqualTo(new Theme(4L, "마크", "노력함", "https://asd.cmom"));
    }

    @Test
    @DisplayName("Theme 테이블의 있는 모든 데이터를 조회한다.")
    void findAll() {
        assertThat(themeRepository.findAll())
                .containsExactly(
                        Fixture.THEME_1,
                        Fixture.THEME_2,
                        Fixture.THEME_3);
    }

    @Test
    @DisplayName("Theme 테이블의 주어진 id와 동일한 데이터를 조회한다.")
    void findById() {
        assertThat(themeRepository.findById(1L))
                .isEqualTo(Optional.of(Fixture.THEME_1));
    }

    @Test
    @DisplayName("Theme 테이블에 주어진 id와 없는 경우 빈 옵셔널을 반환한다.")
    void findById_Return_EmptyOptional() {
        assertThat(themeRepository.findById(99999L))
                .isNotPresent();
    }

    @Test
    @DisplayName("Theme 테이블에서 많이 예약된 테마 10개를 내림차순으로 가져온다.")
    void findOrderByReservation() {
        assertThat(themeRepository.findOrderByReservation())
                .containsExactly(
                        Fixture.THEME_2,
                        Fixture.THEME_1,
                        Fixture.THEME_3);
    }

    @Test
    @DisplayName("주어진 id와 동일한 데이터를 삭제한다.")
    void deleteById() {
        themeRepository.deleteById(3L);
        assertThat(themeRepository.findById(3L)).isNotPresent();
    }
}
