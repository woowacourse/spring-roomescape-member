package roomescape.acceptancetest.theme;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import roomescape.acceptancetest.RoomecapeAcceptanceTest;
import roomescape.acceptancetest.fixture.AcceptanceTestFixture;

@RoomecapeAcceptanceTest
class ThemeAdminApiTest {

    @Autowired
    private AcceptanceTestFixture acceptanceTestFixture;

    @Test
    @DisplayName("테마 목록 조회")
    void findThemes() {
        // given
        acceptanceTestFixture.createTheme("미술관의 밤", "aa", "bb");
        acceptanceTestFixture.createTheme("사라진 열쇠", "aa", "bb");
        acceptanceTestFixture.createTheme("비밀의 방", "aa", "bb");

        // when & then
        RestAssured.given().log().all()
                .when().get("/admin/themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(3));
    }

    @Test
    @DisplayName("테마 생성")
    void createTheme() {
        // given
        Map<String, String> request = new HashMap<>();
        request.put("name", "미술관의 밤");
        request.put("description", "추리 테마");
        request.put("thumbnailUrl", "https://example.com/theme.png");

        // when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/admin/themes")
                .then().log().all()
                .statusCode(201)
                .body("id", is(1));
    }

    @Test
    @DisplayName("테마 생성 시 이름 중복 예외")
    void createTheme_DuplicateName() {
        // given
        acceptanceTestFixture.createTheme("미술관의 밤", "aa", "bb");
        Map<String, String> request = new HashMap<>();
        request.put("name", "미술관의 밤");
        request.put("description", "추리 테마");
        request.put("imageUrl", "https://example.com/theme.png");

        // when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/admin/themes")
                .then().log().all()
                .statusCode(409);
    }

    @Test
    @DisplayName("테마 삭제")
    void deleteTheme() {
        // given
        acceptanceTestFixture.createTheme("미술관의 밤", "aa", "bb");

        // when & then
        RestAssured.given().log().all()
                .when().delete("/admin/themes/1")
                .then().log().all()
                .statusCode(204);
    }

    @Test
    @DisplayName("테마 삭제 시 예약이 존재하면 예외")
    void deleteTheme_ExistReservations() {
        // given
        acceptanceTestFixture.createTheme("미술관의 밤", "aa", "bb");
        acceptanceTestFixture.createReservationTime("10:00", 1L);
        acceptanceTestFixture.createReservation("브라운", acceptanceTestFixture.today(), 1L);

        // when & then
        RestAssured.given().log().all()
                .when().delete("/admin/themes/1")
                .then().log().all()
                .statusCode(409);
    }
}
