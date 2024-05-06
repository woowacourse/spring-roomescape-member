package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.dao.JdbcThemeDao;
import roomescape.domain.theme.Theme;
import roomescape.dto.theme.ThemeCreateRequest;
import roomescape.dto.theme.ThemeResponse;
import roomescape.exception.InvalidValueException;
import roomescape.fixture.ThemeFixtures;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ThemeServiceTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private JdbcThemeDao themeDao;
    @Autowired
    private ThemeService themeService;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("DELETE FROM reservation");
        jdbcTemplate.update("DELETE FROM reservation_time");
        jdbcTemplate.update("DELETE FROM theme");
        jdbcTemplate.execute("ALTER TABLE reservation ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE reservation_time ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE theme ALTER COLUMN id RESTART WITH 1");
    }

    @Test
    @DisplayName("모든 테마 정보를 조회한다.")
    void findAll() {
        //given
        Theme theme1 = ThemeFixtures.createTheme("방탈출1", "방탈출 1번", "섬네일1");
        Theme theme2 = ThemeFixtures.createTheme("방탈출2", "방탈출 2번", "섬네일2");
        themeDao.create(theme1);
        themeDao.create(theme2);

        //when
        List<ThemeResponse> results = themeService.findAll();
        ThemeResponse firstResponse = results.get(0);

        //then
        assertAll(
                () -> assertThat(results).hasSize(2),
                () -> assertThat(firstResponse.name()).isEqualTo("방탈출1"),
                () -> assertThat(firstResponse.description()).isEqualTo("방탈출 1번"),
                () -> assertThat(firstResponse.thumbnail()).isEqualTo("섬네일1")
        );
    }

    @Test
    @DisplayName("테마를 추가한다.")
    void add() {
        //given
        String givenName = "방탈출2";
        String givenDescription = "2번 방탈출";
        String givenThumbnail = "썸네일2";
        ThemeCreateRequest request =
                ThemeFixtures.createThemeCreateRequest(givenName, givenDescription, givenThumbnail);

        //when
        ThemeResponse result = themeService.add(request);

        //then
        assertAll(
                () -> assertThat(result.id()).isSameAs(1L),
                () -> assertThat(result.name()).isEqualTo(givenName),
                () -> assertThat(result.description()).isEqualTo(givenDescription),
                () -> assertThat(result.thumbnail()).isEqualTo(givenThumbnail)
        );
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   ",})
    @DisplayName("테마명이 공백이면 예외가 발생한다.")
    void createThemeByNullOrEmptyName(String given) {
        //given
        ThemeCreateRequest request = ThemeFixtures.createThemeCreateRequest(given, "방탈출 설명", "방탈출 썸네일");

        //when //then
        assertThatThrownBy(() -> themeService.add(request))
                .isInstanceOf(InvalidValueException.class);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   ",})
    @DisplayName("테마 설명이 공백이면 예외가 발생한다.")
    void createThemeByNullOrEmptyDescription(String given) {
        //given
        ThemeCreateRequest request = ThemeFixtures.createThemeCreateRequest("방탈출명", given, "방탈출 썸네일");

        //when //then
        assertThatThrownBy(() -> themeService.add(request))
                .isInstanceOf(InvalidValueException.class);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   ",})
    @DisplayName("테마 썸네일이 공백이면 예외가 발생한다.")
    void createThemeByNullOrEmptyThumbnail(String given) {
        //given
        ThemeCreateRequest request = ThemeFixtures.createThemeCreateRequest("방탈출명", "방탈출 설명", given);

        //when //then
        assertThatThrownBy(() -> themeService.add(request))
                .isInstanceOf(InvalidValueException.class);
    }

    @Test
    @DisplayName("테마를 삭제한다.")
    void delete() {
        //given
        Theme theme = ThemeFixtures.createDefaultTheme();
        themeDao.create(theme);
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
        Theme theme = ThemeFixtures.createDefaultTheme();
        themeDao.create(theme);
        Long givenId = null;

        //when //then
        assertThatThrownBy(() -> themeService.delete(givenId))
                .isInstanceOf(InvalidValueException.class);
    }

    @Test
    @DisplayName("테마 삭제시 아이디가 존재하지 않는다면 예외가 발생한다.")
    void deleteNotExistId() {
        //given
        Theme theme = ThemeFixtures.createDefaultTheme();
        themeDao.create(theme);
        long givenId = 100L;

        //when //then
        assertThatThrownBy(() -> themeService.delete(givenId))
                .isInstanceOf(InvalidValueException.class);
    }
}
