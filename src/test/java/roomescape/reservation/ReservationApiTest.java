package roomescape.reservation;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.jdbc.Sql;
import roomescape.auth.dto.LoginRequest;
import roomescape.global.auth.JwtTokenProvider;
import roomescape.reservation.dto.CreateReservationRequest;
import roomescape.reservation.dto.CreateReservationWithMemberRequest;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@Sql({"/test-schema.sql", "/test-time-data.sql", "/test-theme-data.sql", "/test-member-data.sql",
        "/test-reservation-data.sql"})
public class ReservationApiTest {

    private static final String TOKEN_COOKIE_NAME = "token";

    @DisplayName("예약 생성 API 테스트")
    @Nested
    class CreateReservationTest {

        private static final LocalDate TOMORROW = LocalDate.now().plusDays(1);
        private static final CreateReservationRequest REQUEST = new CreateReservationRequest(TOMORROW, 1L, 1L);
        private static String TOKEN;

        @BeforeEach
        void setUp() {
            TOKEN = RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .body(new LoginRequest("aaa@gmail.com", "1234"))
                    .when().post("/login")
                    .then().log().all()
                    .extract().cookie(TOKEN_COOKIE_NAME);
        }

        @DisplayName("예약 생성을 성공할 경우 201을 반환한다.")
        @Test
        void testCreateReservation() {
            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .cookie(TOKEN_COOKIE_NAME, TOKEN)
                    .body(REQUEST)
                    .when().post("/reservations")
                    .then().log().all()
                    .statusCode(201)
                    .body("id", Matchers.equalTo(4))
                    .body("member.name", Matchers.equalTo("사용자1"));
        }

        @DisplayName("쿠키 정보가 올바르지 않을 경우 401을 반환한다.")
        @Test
        void testInvalidCookie() {
            // 쿠키 없음
            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .body(REQUEST)
                    .when().post("/reservations")
                    .then().log().all()
                    .statusCode(401);
            // JWT 토큰 파싱 불가능
            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .cookie(TOKEN_COOKIE_NAME, "invalidValue")
                    .body(REQUEST)
                    .when().post("/reservations")
                    .then().log().all()
                    .statusCode(401);
            // payload 값으로 식별할 수 없음
            JwtTokenProvider jwtTokenProvider = new JwtTokenProvider();
            String token = jwtTokenProvider.createToken("4");
            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .body(REQUEST)
                    .cookie(TOKEN_COOKIE_NAME, token)
                    .when().post("/reservations")
                    .then().log().all()
                    .statusCode(401);
        }

        @DisplayName("중복 예약을 생성할 경우 400을 반환한다.")
        @Test
        void testDuplicateReservation() {
            // given
            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .cookie(TOKEN_COOKIE_NAME, TOKEN)
                    .body(REQUEST)
                    .when().post("/reservations")
                    .then().log().all()
                    .statusCode(201);
            // when
            // then
            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .cookie(TOKEN_COOKIE_NAME, TOKEN)
                    .body(REQUEST)
                    .when().post("/reservations")
                    .then().log().all()
                    .statusCode(400);
        }
    }

    @DisplayName("관리자 예약 생성 API 테스트")
    @Nested
    class AdminCreateReservationTest {

        private static final LocalDate TOMORROW = LocalDate.now().plusDays(1);
        private static final CreateReservationWithMemberRequest REQUEST = new CreateReservationWithMemberRequest(
                TOMORROW, 1L, 1L, 1L);
        private static String TOKEN;

        @BeforeEach
        void setUp() {
            TOKEN = RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .body(new LoginRequest("admin@gmail.com", "1234"))
                    .when().post("/login")
                    .then().log().all()
                    .extract().cookie(TOKEN_COOKIE_NAME);
        }

        @DisplayName("예약 생성을 성공할 경우 201을 반환한다.")
        @Test
        void testCreateReservation() {
            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .cookie(TOKEN_COOKIE_NAME, TOKEN)
                    .body(REQUEST)
                    .when().post("/admin/reservations")
                    .then().log().all()
                    .statusCode(201)
                    .body("id", Matchers.equalTo(4))
                    .body("member.name", Matchers.equalTo("사용자1"));
        }

        @DisplayName("쿠키 정보가 올바르지 않을 경우 401을 반환한다.")
        @Test
        void testInvalidCookie() {
            // 쿠키 없음
            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .body(REQUEST)
                    .when().post("/admin/reservations")
                    .then().log().all()
                    .statusCode(401);
            // JWT 토큰 파싱 불가능
            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .cookie(TOKEN_COOKIE_NAME, "invalidValue")
                    .body(REQUEST)
                    .when().post("/admin/reservations")
                    .then().log().all()
                    .statusCode(401);
            // payload 값으로 식별할 수 없음
            JwtTokenProvider jwtTokenProvider = new JwtTokenProvider();
            String token = jwtTokenProvider.createToken("4");
            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .body(REQUEST)
                    .cookie(TOKEN_COOKIE_NAME, token)
                    .when().post("/admin/reservations")
                    .then().log().all()
                    .statusCode(401);
        }

        @DisplayName("중복 예약을 생성할 경우 400을 반환한다.")
        @Test
        void testDuplicatedReservation() {
            // given
            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .cookie(TOKEN_COOKIE_NAME, TOKEN)
                    .body(REQUEST)
                    .when().post("/admin/reservations")
                    .then().log().all()
                    .statusCode(201);
            // when
            // then
            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .cookie(TOKEN_COOKIE_NAME, TOKEN)
                    .body(REQUEST)
                    .when().post("/admin/reservations")
                    .then().log().all()
                    .statusCode(400);
        }
    }
}
