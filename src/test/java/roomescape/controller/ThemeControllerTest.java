package roomescape.controller;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.dto.request.ThemeRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ThemeControllerTest {

    @Test
    @DisplayName("테마 생성 요청은 201을 반환한다")
    void addTheme() {
        ThemeRequest req = new ThemeRequest(
                "Ddyong",
                "살인마가 쫓아오는 느낌",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
        );

        ApiTestHelper.post("/themes", req)
                .statusCode(201)
                .body("name", is("Ddyong"))
                .body("description", containsString("살인마"))
                .body("thumbnail", is(req.thumbnail()));
    }

    @Test
    @DisplayName("테마 조회 요청은 전체 테마 리스트를 반환한다")
    void getThemes() {
        ApiTestFixture.createTheme(
                "A", "descA", "urlA"
        );
        ApiTestFixture.createTheme(
                "B", "descB", "urlB"
        );

        ApiTestHelper.get("/themes")
                .statusCode(200)
                .body("size()", is(2));
    }

    @Test
    @DisplayName("테마 삭제 요청은 204를 반환한다")
    void deleteTheme() {
        long id = ApiTestHelper.post("/themes",
                        new ThemeRequest("X", "descX", "urlX"))
                .statusCode(201)
                .extract().jsonPath().getLong("id");

        ApiTestHelper.delete("/themes/" + id)
                .statusCode(204);

        ApiTestHelper.get("/themes")
                .statusCode(200)
                .body("size()", is(0));
    }

}
