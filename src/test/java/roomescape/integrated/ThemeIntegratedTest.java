package roomescape.integrated;

import static org.hamcrest.CoreMatchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ThemeIntegratedTest {

    @Test
    @DisplayName("테마 페이지를 렌더링한다.")
    void displayThemeAdmin() {
        RestAssured.given().log().all()
                .when().get("/admin/theme")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("전체 테마 데이터를 조회한다.")
    void readAllTheme() {
        setUpTheme();

        RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @Test
    @DisplayName("테마 데이터를 추가한다.")
    void createTheme() {
        //given
        Map<String, Object> reservation = new HashMap<>();
        reservation.put("name", "레벨2 탈출");
        reservation.put("description", "우테코 레벨2를 탈출하는 내용입니다.");
        reservation.put("thumbnail", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");

        //when
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201)
                .body("id", is(1),
                        "name", is("레벨2 탈출"),
                        "description", is("우테코 레벨2를 탈출하는 내용입니다."),
                        "thumbnail", is("https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg")
                );
    }

    @Test
    @DisplayName("테마 데이터를 삭제한다.")
    void deleteTheme() {
        setUpTheme();

        RestAssured.given().log().all()
                .when().delete("/themes/1")
                .then().log().all()
                .statusCode(204);
    }

    private void setUpTheme() {
        Map<String, Object> reservation = new HashMap<>();
        reservation.put("name", "레벨2 탈출");
        reservation.put("description", "우테코 레벨2를 탈출하는 내용입니다.");
        reservation.put("thumbnail", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/themes");
    }
}
