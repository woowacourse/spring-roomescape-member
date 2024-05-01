package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.dao.WebThemeDao;
import roomescape.domain.theme.Theme;
import roomescape.domain.theme.ThemeDescription;
import roomescape.domain.theme.ThemeName;
import roomescape.domain.theme.ThemeThumbnail;
import roomescape.dto.theme.ThemeCreateRequest;
import roomescape.dto.theme.ThemeResponse;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ThemeServiceTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private WebThemeDao themeDao;
    @Autowired
    private ThemeService themeService;

    @BeforeEach
    void setUp() {
        themeDao.create(new Theme(null, ThemeName.from("방탈출1"), ThemeDescription.from("방탈출 1번"), ThemeThumbnail.from("섬네일1")));
    }

    @AfterEach
    void tearDown() {
        jdbcTemplate.update("DELETE FROM theme");
        jdbcTemplate.execute("ALTER TABLE theme ALTER COLUMN id RESTART WITH 1");
    }

    @Test
    @DisplayName("모든 테마 정보를 조회한다.")
    void findAll() {
        //when
        List<ThemeResponse> results = themeService.findAll();
        ThemeResponse firstResponse = results.get(0);

        //then
        assertAll(
                () -> assertThat(results).hasSize(1),
                () -> assertThat(firstResponse.getName()).isEqualTo("방탈출1"),
                () -> assertThat(firstResponse.getDescription()).isEqualTo("방탈출 1번"),
                () -> assertThat(firstResponse.getThumbnail()).isEqualTo("섬네일1")
        );
    }

    @Test
    @DisplayName("테마를 추가한다.")
    void add() {
        //given
        String givenName = "방탈출2";
        String givenDescription = "2번 방탈출";
        String givenThumbnail = "썸네일2";
        ThemeCreateRequest request = ThemeCreateRequest.of(givenName, givenDescription, givenThumbnail);

        //when
        ThemeResponse result = themeService.add(request);

        //then
        assertAll(
                () -> assertThat(result.getName()).isEqualTo(givenName),
                () -> assertThat(result.getDescription()).isEqualTo(givenDescription),
                () -> assertThat(result.getThumbnail()).isEqualTo(givenThumbnail)
        );
    }

    @Test
    @DisplayName("테마를 삭제한다.")
    void delete() {
        //given
        long givenId = 1L;

        //when
        themeService.delete(givenId);
        List<ThemeResponse> results = themeService.findAll();

        //then
        assertThat(results).isEmpty();
    }

    @Test
    @DisplayName("테마 삭제시 아이디가 비어있으면 예외가 발생한다.")
    void deleteNullId() {
        //given
        Long givenId = null;

        //when //then
        assertThatThrownBy(() -> themeService.delete(givenId))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테마 삭제시 아이디가 존재하지 않는다면 예외가 발생한다.")
    void deleteNotExistId() {
        //given
        long givenId = 100L;

        //when //then
        assertThatThrownBy(() -> themeService.delete(givenId))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
