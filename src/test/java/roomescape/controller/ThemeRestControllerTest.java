package roomescape.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.dto.ThemeResponse;
import roomescape.service.ThemeService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ThemeRestControllerTest {

    @Autowired
    private ThemeService themeService;

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("모든 테마를 조회한다.")
    @Test
    void getAll() {
        // when
        List<ThemeResponse> allThemes = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().get("/themes")
                .then().log().all()
                .statusCode(HttpStatus.SC_OK).extract()
                .jsonPath().getList(".", ThemeResponse.class);

        // then
        assertThat(allThemes).hasSize(11);
    }

    @DisplayName("인기 테마를 조회한다.")
    @Test
    void getRank() {
        // when
        List<ThemeResponse> allThemes = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().get("/themes/ranking")
                .then().log().all()
                .statusCode(HttpStatus.SC_OK).extract()
                .jsonPath().getList(".", ThemeResponse.class);

        // then
        assertThat(allThemes).doesNotContain(new ThemeResponse(11L, "토끼와 거북이", "코믹",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS6cL_syJHIrZvLLdQSnhzzQkm2Q0em6iPwbW4UH2J4Aw&s"));
    }

    @DisplayName("테마를 생성한다.")
    @Test
    void create() {
        // given
        Map<String, String> params = Map.of(
                "name", "새테마",
                "description", "설명",
                "thumbnail", "썸네일"
        );

        // when
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/themes")
                .then().log().all()
                .statusCode(HttpStatus.SC_CREATED);

        List<ThemeResponse> allThemes = themeService.findAll();

        // then
        assertAll(
                () -> assertThat(allThemes).hasSize(12),
                () -> assertThat(allThemes).contains(new ThemeResponse(12L, "새테마", "설명", "썸네일"))
        );
    }

    @DisplayName("중복된 테마를 생성하려고 하면 BAD_REQUEST를 반환한다.")
    @Test
    void create_duplicate_badRequest() {
        // given
        Map<String, String> params = Map.of(
                "name", "토끼와 거북이",
                "description", "코믹",
                "thumbnail", "썸네일"
        );

        // when && then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/themes")
                .then().log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @DisplayName("해당 id의 테마를 삭제한다.")
    @Test
    void deleteById() {
        // when
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().delete("/themes/11")
                .then().log().all()
                .statusCode(HttpStatus.SC_NO_CONTENT);

        List<ThemeResponse> allThemes = themeService.findAll();

        // then
        assertAll(
                () -> assertThat(allThemes).hasSize(10),
                () -> assertThat(allThemes).doesNotContain(new ThemeResponse(11L, "토끼와 거북이", "코믹",
                        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS6cL_syJHIrZvLLdQSnhzzQkm2Q0em6iPwbW4UH2J4Aw&s"))
        );
    }

    @DisplayName("예약이 존재하는 테마를 삭제하려고 하면 BAD_REQUEST를 반환한다.")
    @Test
    void deleteById_existReservation_badRequest() {
        // when
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().delete("/themes/1")
                .then().log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }
}
