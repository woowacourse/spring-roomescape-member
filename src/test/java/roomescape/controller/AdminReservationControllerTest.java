package roomescape.controller;

import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import io.restassured.RestAssured;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.test.util.TestDatabaseUtils;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AdminReservationControllerTest {

    private static final int INITIALIZED_RESERVATION_COUNT = 3;

    @Nested
    class 모든_예약을_조회한다 {

        @Autowired
        private JdbcTemplate jdbcTemplate;

        @Test
        void 존재하는_모든_예약에_대한_상세_정보를_조회한다() {
            RestAssured.given().log().all()
                    .when().get("/admin/reservations")
                    .then().log().all()
                    .statusCode(200)
                    .body("size()", is(INITIALIZED_RESERVATION_COUNT))
                    .body("[0].id", notNullValue())
                    .body("[0].name", notNullValue())
                    .body("[0].date", notNullValue())
                    .body("[0].time.id", notNullValue())
                    .body("[0].time.startAt", notNullValue())
                    .body("[0].theme.id", notNullValue())
                    .body("[0].theme.name", notNullValue())
                    .body("[0].cancelable", any(Boolean.class));
        }

        @Test
        void 예약이_없다면_빈_리스트를_응답한다() {
            TestDatabaseUtils.clearTables(jdbcTemplate);

            RestAssured.given().log().all()
                    .when().get("/admin/reservations")
                    .then().log().all()
                    .statusCode(200)
                    .body("size()", is(0));
        }
    }

    @Nested
    class 예약을_취소한다 {

        @Test
        void 식별자를_기반으로_예약을_취소한다() {
            String reservationIdToDelete = "cccccccc-cccc-cccc-cccc-cccccccccc01";

            RestAssured.given().log().all()
                    .when().delete("/admin/reservations/" + reservationIdToDelete)
                    .then().log().all()
                    .statusCode(200);

            RestAssured.given().log().all()
                    .when().get("/admin/reservations")
                    .then().log().all()
                    .statusCode(200)
                    .body("size()", is(INITIALIZED_RESERVATION_COUNT - 1));
        }

        @Test
        void 식별자로_예약을_찾을_수_없다면_400을_응답한다() {
            String nonExistentReservationId = "ffffffff-ffff-ffff-ffff-ffffffffffff";

            RestAssured.given().log().all()
                    .when().delete("/admin/reservations/" + nonExistentReservationId)
                    .then().log().all()
                    .statusCode(400);
        }
    }
}
