package roomescape.reservation.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.reservation.controller.dto.ThemeRankingResponse;
import roomescape.reservation.controller.dto.ThemeResponse;
import roomescape.reservation.domain.Theme;
import roomescape.reservation.repository.ThemeRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ThemeIntegrationTest {

    @Autowired
    private ThemeRepository themeRepository;

    @DisplayName("테마 목록의 조회 시 DB에 저장된 테마 목록을 반환한다")
    @Test
    void get_themes_test() {
        // when
        List<ThemeResponse> themes = RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .statusCode(200).extract()
                .jsonPath().getList(".", ThemeResponse.class);

        // then
        List<Theme> savedThemes = themeRepository.findAll();
        assertAll(
                () -> assertThat(themes).hasSize(savedThemes.size()),
                () -> assertThat(themes).extracting(ThemeResponse::name)
                        .containsExactlyInAnyOrderElementsOf(
                                savedThemes.stream()
                                        .map(Theme::getName)
                                        .toList()
                        ),
                () -> assertThat(themes).extracting(ThemeResponse::description)
                        .containsExactlyInAnyOrderElementsOf(
                                savedThemes.stream()
                                        .map(Theme::getDescription)
                                        .toList()
                        )
        );

    }

    @DisplayName("인기 테마의 목록을 조회한다")
    @Test
    void get_theme_rankings_test() {
        // when
        List<ThemeRankingResponse> themeRanks = RestAssured.given().log().all()
                .when().get("/themes/ranking")
                .then().log().all()
                .statusCode(200).extract()
                .jsonPath().getList(".", ThemeRankingResponse.class);

        // then
        assertThat(themeRanks).containsExactly(
                new ThemeRankingResponse("레벨1 탈출", "우테코 레벨1를 탈출하는 내용입니다.",
                        "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"),
                new ThemeRankingResponse("레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.",
                        "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"),
                new ThemeRankingResponse("레벨3 탈출", "우테코 레벨3를 탈출하는 내용입니다.",
                        "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg")
        );
    }

    @DisplayName("테마를 생성하면 DB에 테마 데이터가 저장된다")
    @Test
    void add_theme_test() {
        //given
        Map<String, String> params = new HashMap<>();
        params.put("name", "레벨7 탈출");
        params.put("description", "우테코 레벨7를 탈출하는 내용입니다.");
        params.put("thumbnail", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");

        // when
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201)
                .body("id", is(7));

        // then
        Theme savedTheme = themeRepository.findById(7L).get();

        assertAll(
                () -> assertThat(savedTheme.getId()).isEqualTo(7L),
                () -> assertThat(savedTheme.getName()).isEqualTo("레벨7 탈출"),
                () -> assertThat(savedTheme.getDescription()).isEqualTo("우테코 레벨7를 탈출하는 내용입니다."),
                () -> assertThat(savedTheme.getThumbnail()).isEqualTo(
                        "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg")
        );
    }

    @DisplayName("테마를 삭제하면 DB의 테마 데이터가 삭제된다")
    @Test
    void delete_theme_test() {
        // when
        RestAssured.given().log().all()
                .when().delete("/themes/6")
                .then().log().all()
                .statusCode(204);

        // then
        assertAll(
                () -> assertThat(themeRepository.findAll()).hasSize(5),
                () -> assertThat(themeRepository.findById(6L).isEmpty()).isTrue()
        );
    }

    @DisplayName("테마 생성 시 입력 값이 존재하지 않거나 공백이면 예외가 발생한다")
    @MethodSource
    @ParameterizedTest
    void add_theme_null_empty_exception(Map<String, String> requestBody) {
        // when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when().post("/themes")
                .then().log().all()
                .statusCode(400)
                .body(equalTo("요청 형식이 올바르지 않습니다."));
    }

    @DisplayName("테마 삭제 시 연관된 예약 데이터가 존재하여 예외가 발생한다.")
    @Test
    void delete_theme_exception() {
        // when & then
        RestAssured.given().log().all()
                .when().delete("/themes/3")
                .then().log().all()
                .statusCode(400)
                .body(equalTo("해당 테마와 연관된 예약이 있어 삭제할 수 없습니다."));
    }

    static Stream<Arguments> add_theme_null_empty_exception() {
        return Stream.of(
                Arguments.of(
                        Map.of(
                                "name", "루키",
                                "description", ""
                        )
                ),
                Arguments.of(
                        Map.of(
                                "name", "루키"
                        )
                ),
                Arguments.of(
                        Map.of(
                                "name", "",
                                "description", "우테코 레벨3를 탈출하는 내용입니다."
                        )
                ),
                Arguments.of(
                        Map.of(
                                "description", "우테코 레벨3를 탈출하는 내용입니다."
                        )
                )
        );
    }

}
