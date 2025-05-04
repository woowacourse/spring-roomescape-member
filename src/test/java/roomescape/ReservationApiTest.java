package roomescape;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import roomescape.presentation.dto.request.ReservationRequest;

@ActiveProfiles("test")
@Sql(scripts = {"/schema.sql", "/test.sql"})
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
class ReservationApiTest {

    @Test
    void 어드민_페이지로_접근할_수_있다() {
        RestAssured.given().log().all()
                .when().get("/admin")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    void 어드민이_예약_관리_페이지에_접근한다() {
        RestAssured.given().log().all()
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    void 모든_예약_정보를_반환한다() {
        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(6));
    }

    @Test
    void 존재하지_않는_예약을_삭제할_경우_NOT_FOUND_반환() {
        RestAssured.given().log().all()
                .when().delete("/reservations/7")
                .then().log().all()
                .statusCode(404);
    }

    @Test
    void 시간을_추가한뒤_예약을_추가한다() {
        ReservationRequest request = new ReservationRequest(
                "브라운",
                LocalDate.now().plusDays(1),
                1L,
                1L
        );

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(7));
    }

    @Test
    void 예약날짜는_null을_받을_수_없다() {
        ReservationRequest request = new ReservationRequest("브라운", null, 1L, 1L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "abcdefghijk"})
    void 예약자_이름은_빈_값을_받을_수_없다(String name) {
        ReservationRequest request = new ReservationRequest(name, LocalDate.now().plusDays(1), 1L, 1L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    void 예약_시간_id는_null을_받을_수_없다() {
        ReservationRequest request = new ReservationRequest("브라운", LocalDate.now().plusDays(1), null, 1L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    void 과거날짜로_예약을_하면_에러를_반환한다() {
        ReservationRequest request = new ReservationRequest(
                "브라운",
                LocalDate.now().minusDays(10),
                1L,
                1L
        );

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400)
                .body(equalTo("현재보다 과거의 날짜로 예약 할 수 없습니다."));
    }

    @Test
    void 중복된_시간에_예약을_하면_에러가_발생한다() {
        ReservationRequest request1 = new ReservationRequest(
                "브라운",
                LocalDate.now().plusDays(10),
                1L,
                1L
        );

        ReservationRequest request2 = new ReservationRequest(
                "드라고",
                LocalDate.now().plusDays(10),
                1L,
                1L
        );

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request1)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request2)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(409);
    }
}
