package roomescape.integrate.mission;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.is;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Disabled
public class MissionStep1Test {

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    void setup() {
        RestAssuredMockMvc.webAppContextSetup(context);
    }

    @Test
    void 일단계() {
        given()
                .when().get("/")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 이단계_예약_어드민페이지에_접근가능하다() {
        given()
                .when().get("/admin/reservation")
                .then()
                .statusCode(HttpStatus.OK.value());

        given()
                .when().get("/reservations")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(0));
    }

    @Nested
    class 삼단계 {

        static Map<String, String> params;

        static {
            params = new HashMap<>();
            params.put("name", "브라운");
            params.put("date", LocalDate.now().plusDays(1).toString());
            params.put("time", "15:40");
        }

        @Test
        void 삼단계_예약을_할_수_있다() {
            given()
                    .contentType("application/json")
                    .body(params)
                    .when().post("/reservations")
                    .then()
                    .statusCode(HttpStatus.CREATED.value());

            given()
                    .when().get("/reservations")
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("size()", is(1));
        }

        @Test
        void 삼단계_예약을_삭제할_수_있다() {
            given()
                    .contentType("application/json")
                    .body(params)
                    .when().post("/reservations")
                    .then()
                    .statusCode(HttpStatus.CREATED.value());

            given()
                    .when().delete("/reservations/1")
                    .then()
                    .statusCode(HttpStatus.NO_CONTENT.value());

            given()
                    .when().get("/reservations")
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("size()", is(0));
        }
    }
}
