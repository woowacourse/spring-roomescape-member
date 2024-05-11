package roomescape.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.jdbc.Sql;
import roomescape.domain.reservation.Theme;
import roomescape.repository.rowmapper.ThemeRowMapper;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@JdbcTest
@Sql("/truncate.sql")
@ExtendWith(MockitoExtension.class)
class ThemeDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;

    private RowMapper<Theme> rowMapper;
    private ThemeDao themeDao;


    @BeforeEach
    void setUp() {
        rowMapper = new ThemeRowMapper();
        themeDao = new ThemeDao(jdbcTemplate, dataSource, rowMapper);
        jdbcTemplate.update("insert into theme(theme_name, description, thumbnail) values ('공포', '공포입니다.', '123')");
    }

    @AfterEach
    void clearTable() {
        jdbcTemplate.update("DELETE FROM theme");
    }

    @Test
    @DisplayName("테마를 저장할 수 있다")
    void should_SaveTheme() {
        //given
        int expectedSize = 2;
        Theme theme = new Theme(null, "테마1", "테마입니다", "테마입니다.");

        //when
        Theme savedTheme = themeDao.save(theme);

        //then
        String sql = "SELECT * FROM theme";
        List<Theme> themes = jdbcTemplate.query(sql, rowMapper);
        assertThat(themes.size()).isEqualTo(expectedSize);
        assertThat(savedTheme.getId()).isNotNull();
    }

    @Test
    @DisplayName("중복된 이름의 테마를 저장할 수 없다.")
    void should_ThrowIllegalStateException_WhenGiveDuplicatedNameTheme() {
        //given
        Theme theme = new Theme(null, "공포", "테마입니다", "테마입니다.");

        //when-then
        assertThatThrownBy(() -> themeDao.save(theme))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("[ERROR] 키 값 에러 : 중복된 테마 키가 존재합니다.");
    }

    @Test
    @DisplayName("중복된 설명의 테마를 저장할 수 없다.")
    void should_ThrowIllegalStateException_WhenGiveDuplicatedDescriptionTheme() {
        //given
        Theme theme = new Theme(null, "테마1", "공포입니다.", "테마입니다.");

        //when-then
        assertThatThrownBy(() -> themeDao.save(theme))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("[ERROR] 키 값 에러 : 중복된 테마 키가 존재합니다.");
    }

    @Test
    @DisplayName("중복된 섬네일 링크를 지닌 테마를 저장할 수 없다.")
    void should_ThrowIllegalStateException_WhenGiveDuplicateThumbnailTheme() {
        //given
        Theme theme = new Theme(null, "테마1", "테마입니다.", "123");

        //when-then
        assertThatThrownBy(() -> themeDao.save(theme))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("[ERROR] 키 값 에러 : 중복된 테마 키가 존재합니다.");
    }

    @Test
    @DisplayName("모든 테마를 조회할 수 있다")
    void should_getAllTheme() {
        int expectedSize = 1;
        assertThat(themeDao.getAll()).hasSize(expectedSize);
    }

    @Test
    @DisplayName("특정 id값을 지닌 테마를 찾을 수 있다")
    void should_findTheme_When_GiveId() {
        //given
        int findId = 1;
        String expectedName = "공포";
        String expectedDescription = "공포입니다.";
        String expectedThumbnail = "123";

        //when
        Optional<Theme> foundTheme = themeDao.findById(findId);

        //then
        assertThat(foundTheme.isPresent()).isTrue();
        assertThat(foundTheme.get().getName()).isEqualTo(expectedName);
        assertThat(foundTheme.get().getDescription()).isEqualTo(expectedDescription);
        assertThat(foundTheme.get().getThumbnail()).isEqualTo(expectedThumbnail);
    }

    @Test
    @DisplayName("특정 id값을 지닌 테마를 삭제할 수 있다")
    void should_deleteTheme_When_GiveId() {
        //given
        int deleteId = 1;

        // when
        themeDao.delete(deleteId);

        //then
        String sql = "SELECT * FROM theme";
        List<Theme> themes = jdbcTemplate.query(sql, rowMapper);
        assertThat(themes.size()).isZero();
    }
}