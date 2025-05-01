package roomescape;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class RoomEscapeApplicationTest {

    @Test
    @DisplayName("존재하지 않는 ID로 삭제 요청 시 404 응답이 반환되어야 한다")
    void deleteNonExistingReservation() {
        RestAssured.given().log().all()
                .when().delete("/reservations/999")
                .then().log().all()
                .statusCode(404)
                .body("body.detail", equalTo("[ERROR] 예약 데이터를 찾을 수 없습니다:999"));
    }

    @Test
    @DisplayName("테마 목록을 조회한다.")
    void readAllThemes() {
        RestAssured.given().log().all()
                .when().get("/themes/popular")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(10))
                .body("[0].name", is("우테코 레벨1 탈출"))
                .body("[1].name", is("우테코 레벨2 탈출"))
                .body("[2].name", is("우테코 레벨3 탈출"))
                .body("[3].name", is("우테코 레벨4 탈출"))
                .body("[4].name", is("우테코 레벨5 탈출"))
                .body("[5].name", is("우테코 레벨6 탈출"))
                .body("[6].name", is("우테코 레벨7 탈출"))
                .body("[7].name", is("우테코 레벨8 탈출"))
                .body("[8].name", is("우테코 레벨9 탈출"))
                .body("[9].name", is("우테코 레벨10 탈출"));
    }
}
