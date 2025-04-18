package roomescape.reservation;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import roomescape.reservation.entity.Reservation;
import roomescape.reservation.repository.ReservationRepository;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationApiTest {

    @Autowired
    private ReservationRepository reservationRepository;

    @DisplayName("어드민 페이지로 접근할 수 있다.")
    @Test
    void test1() {
        RestAssured.given().log().all()
                .when().get("/admin")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("어드민이 예약 관리 페이지에 접근한다.")
    @Test
    void test2() {
        RestAssured.given().log().all()
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("모든 예약 정보를 반환한다.")
    @Test
    void test3() {
        LocalDateTime now = LocalDateTime.now();

        List<Reservation> reservations = List.of(
                new Reservation(1, "꾹", now),
                new Reservation(2, "꾹", now),
                new Reservation(3, "꾹", now)
        );

        for (Reservation reservation : reservations) {
            reservationRepository.save(reservation);
        }

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(3));
    }

    @DisplayName("예약 정보를 추가한다.")
    @Test
    void test4() {

        String name = "브라운";
        String date = "2023-08-05";
        String time = "15:40";

        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("date", date);
        params.put("time", time);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("id", is(1),
                        "name", is(name),
                        "date", is(date),
                        "time", is(time)
                );
    }

    @DisplayName("예약 삭제")
    @Nested
    class delete {

        @DisplayName("예약을 삭제한다.")
        @Test
        void test1() {
            long id = 1L;

            // TODO get Reservation 경우에는 데이터 3개가 문제없이 저장되었지만, delete를 할 때는 repository에 존재하지 않음
//            Reservation reservation = new Reservation(id, "브라운", LocalDateTime.now());
//            reservationRepository.save(reservation);

            Map<String, String> params = new HashMap<>();
            params.put("name", "브라운");
            params.put("date", "2023-08-05");
            params.put("time", "15:40");

            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .body(params)
                    .when().post("/reservations")
                    .then().log().all()
                    .statusCode(200)
                    .body("id", is((int) id));


            RestAssured.given().log().all()
                    .when().delete("/reservations/" + id)
                    .then().log().all()
                    .statusCode(204);
        }

        @DisplayName("존재하지 않는 예약을 삭제할 경우 NOT_FOUND 반환")
        @Test
        void test2() {
            RestAssured.given().log().all()
                    .when().delete("/reservations/4")
                    .then().log().all()
                    .statusCode(404);
        }
    }
}
