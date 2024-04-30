package roomescape.repository.theme;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import roomescape.domain.Name;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@JdbcTest
@Import(ThemeH2Repository.class)
@Sql("/initial_test_data.sql")
class ThemeH2RepositoryTest {

    @Autowired
    private ThemeH2Repository themeH2Repository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @DisplayName("Theme를 저장한다.")
    void save() {
        Theme theme = new Theme(null, new Name("레벨2"), "레벨2 설명", "레벨2 썸네일");

        Theme saved = themeH2Repository.save(theme);

        assertThat(saved.getId()).isNotNull();
    }

    @Test
    @DisplayName("중복된 테마를 저장하려고 하면 예외가 발생한다.")
    void saveDuplicatedTime() {
        Theme theme = new Theme(null, new Name("레벨2 탈출"), "레벨2 설명", "레벨2 썸네일"); // TODO 이름만 같으면 같다고 했음

        assertThatThrownBy(() -> themeH2Repository.save(theme))
                .isInstanceOf(IllegalArgumentException.class);
    }


    @Test
    @DisplayName("id에 맞는 Theme을 제거한다.")
    void delete() {
        themeH2Repository.delete(11L);

        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM theme", Integer.class);

        assertThat(count).isEqualTo(1);
    }

    @Test
    @DisplayName("참조되어 있는 테마를 삭제하는 경우 예외가 발생한다.")
    void deleteReferencedTime() {
        assertThatThrownBy(() -> themeH2Repository.delete(10L))
                .isInstanceOf(IllegalArgumentException.class);
    }


    @Test
    @DisplayName("모든 theme를 찾는다.")
    void findAll() {
        List<Theme> found = themeH2Repository.findAll();

        assertThat(found).hasSize(2);
    }

    @Test
    @DisplayName("id에 맞는 theme를 찾는다.")
    void findBy() {
        Theme found = themeH2Repository.findById(10L).get();

        assertThat(found.getName()).isEqualTo(new Name("레벨2 탈출"));
    }

    @Test
    @DisplayName("존재하지 않는 id가 들어오면 빈 Optional 객체를 반환한다.")
    void findEmpty() {
        Optional<Theme> theme = themeH2Repository.findById(-1L);

        assertThat(theme.isEmpty()).isTrue();
    }
}
