package roomescape.controller.theme;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import roomescape.dao.theme.FakeThemeDaoImpl;
import roomescape.dao.TestDaoConfiguration;
import roomescape.domain.theme.Theme;
import roomescape.dto.theme.request.ThemeRequestDto;
import roomescape.dto.theme.response.ThemeResponseDto;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = "spring.sql.init.mode=never")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Import(TestDaoConfiguration.class)
class ThemeControllerTest {

    @Autowired
    private FakeThemeDaoImpl themeDao;

    @LocalServerPort
    int port;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
    }

    @DisplayName("테마를 등록하면 응답 테마가 반환된다.")
    @Test
    void saveTheme() {
        //given
        ThemeRequestDto request = new ThemeRequestDto("테마", "설명", "http://썸네일");

        //when
        ThemeResponseDto actual = RestAssured
                .given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/themes")
                .then()
                .log().all()
                .statusCode(201)
                .extract()
                .as(ThemeResponseDto.class);

        //then
        assertThat(actual)
                .extracting("id", "name", "description", "thumbnail")
                .containsExactly(1L, "테마", "설명", "http://썸네일");
    }

    @DisplayName("등록된 모든 테마를 조회하면 응답 테마 리스트가 반환된다.")
    @Test
    void readThemes() {
        //given
        themeDao.saveTheme(new Theme(1L, "테마", "설명", "http://썸네일"));
        themeDao.saveTheme(new Theme(2L, "테마1", "설명1", "http://썸네일1"));

        //when
        List<ThemeResponseDto> actual = RestAssured
                .given()
                .log().all()
                .contentType(ContentType.JSON)
                .when()
                .get("/themes")
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .as(new TypeRef<>() {
                });

        //then
        List<ThemeResponseDto> compareList = List.of(
                new ThemeResponseDto(1L, "테마", "설명", "http://썸네일"),
                new ThemeResponseDto(2L, "테마1", "설명1", "http://썸네일1")
        );
        assertThat(actual)
                .hasSize(2)
                .isEqualTo(compareList);
    }

    @DisplayName("등록된 테마를 삭제할 수 있다.")
    @Test
    void deleteTheme() {
        //given
        themeDao.saveTheme(new Theme(1L, "테마", "설명", "http://썸네일"));

        //when
        RestAssured
                .given()
                .log().all()
                .contentType(ContentType.JSON)
                .when()
                .delete("/themes/1")
                .then()
                .log().all()
                .statusCode(204);

        //then
        List<Theme> actual = themeDao.findAllTheme();
        assertThat(actual).hasSize(0);
    }
}
