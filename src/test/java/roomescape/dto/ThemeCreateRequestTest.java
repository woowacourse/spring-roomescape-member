package roomescape.dto;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.Map;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ThemeCreateRequestTest {

    @LocalServerPort
    private Integer port;

    @BeforeEach
    void setPort() {
        RestAssured.port = port;
    }

    @DisplayName("이름이 비어있을 경우 BAD REQUEST를 반환한다.")
    @Test
    void create_nullnName_badRequest() {
        // given
        Map<String, String> params = Map.of(
                "name", "",
                "description", "설명",
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

    @DisplayName("설명이 비어있을 경우 BAD REQUEST를 반환한다.")
    @Test
    void create_nullDescription_badRequest() {
        // given
        Map<String, String> params = Map.of(
                "name", "테니",
                "description", "",
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

    @DisplayName("썸네일이 비어있을 경우 BAD REQUEST를 반환한다.")
    @Test
    void create_nullThumbnail_badRequest() {
        // given
        Map<String, String> params = Map.of(
                "name", "테니",
                "description", "설명",
                "thumbnail", ""
        );

        // when && then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/themes")
                .then().log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }
}
