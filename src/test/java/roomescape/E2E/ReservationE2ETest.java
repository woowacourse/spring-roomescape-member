package roomescape.E2E;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.util.Date;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import roomescape.controller.dto.request.CreateReservationRequest;
import roomescape.domain.MemberRoleType;
import roomescape.jwt.JwtProvider;
import roomescape.jwt.JwtRequest;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@Sql(scripts = "/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/reservation-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationE2ETest {

    @Autowired
    JwtProvider jwtProvider;

    String token;

    @BeforeEach
    void init() {
        token = jwtProvider.generateToken(new JwtRequest(2, "admin", MemberRoleType.ADMIN, new Date()));
    }

    @Test
    @DisplayName("예약을 추가한다")
    void saveReservation() {

        CreateReservationRequest request = new CreateReservationRequest(LocalDate.of(3000, 1, 1), 1, 1);

        RestAssured.given().log().all()
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .body("id", is(2));
    }

    @Test
    @DisplayName("테마, 날짜, 시간이 같은 예약이 존재하면 409를 응답한다")
    void exceptionWhenSameDateTime() {
        //given
        LocalDate date = LocalDate.of(2100, 1, 1);
        CreateReservationRequest request = new CreateReservationRequest(date, 1, 1);

        RestAssured.given().log().all()
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(409);
    }

    @Test
    @DisplayName("과거 시점으로 예약하는 경우 422를 응답한다")
    void exceptionWhenPastDate() {
        // given
        LocalDate date = LocalDate.of(2000, 1, 1);
        CreateReservationRequest request = new CreateReservationRequest(date, 1, 1);

        //when & then
        RestAssured.given().log().all()
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(422);
    }

    @Test
    @DisplayName("존재하지 않는 timeId인 경우 404를 응답한다")
    void throwExceptionWhenNotExistTimeId() {
        //given
        CreateReservationRequest request = new CreateReservationRequest(LocalDate.of(3000, 1, 1), 100, 1);

        RestAssured.given().log().all()
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(404);
    }

    @Test
    @DisplayName("존재하지 않는 themeId 경우 404를 응답한다")
    void throwExceptionWhenNotExistThemeId() {
        //given
        CreateReservationRequest request = new CreateReservationRequest(LocalDate.of(3000, 1, 1), 1, 100);

        RestAssured.given().log().all()
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(404);
    }

    @Test
    @DisplayName("예약을 조회한다")
    void findReservation() {
        RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("", hasSize(1));
    }

    @Test
    @DisplayName("예약을 삭제한다")
    void removeReservationById() {
        RestAssured.given().log().all()
                .cookie("token", token)
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(204);
    }

    @Test
    @DisplayName("존재하지 않는 예약을 삭제하려는 경우 예외를 던진다")
    void removeNotExistReservationById() {
        RestAssured.given().log().all()
                .cookie("token", token)
                .when().delete("/reservations/100")
                .then().log().all()
                .statusCode(404);
    }

    @ParameterizedTest
    @MethodSource("provideCriteria")
    @DisplayName("필터 기준을 기반으로 예약을 조회한다")
    void findReservationByCriteria(Map<String, Object> params, int expectedSize) {

        RestAssured.given().log().all()
                .cookie("token", token)
                .params(params)
                .when().get("/admin/reservations/search")
                .then().log().all()
                .statusCode(200)
                .body("", hasSize(expectedSize));
    }

    private static Stream<Arguments> provideCriteria() {
        return Stream.of(
                Arguments.of(
                        Map.of(
                                "memberId", 1L,
                                "themeId", 1L,
                                "dateFrom", "2025-01-01",
                                "dateTo", "2025-01-01"
                        ),
                        1
                ),
                Arguments.of(
                        Map.of(
                                "themeId", 1L,
                                "dateFrom", "2025-01-01",
                                "dateTo", "2025-01-01"
                        ),
                        1
                ),
                Arguments.of(
                        Map.of(
                                "memberId", 1L,
                                "dateFrom", "2025-01-01",
                                "dateTo", "2025-01-01"
                        ),
                        1
                ),
                Arguments.of(
                        Map.of(
                                "memberId", 1L,
                                "themeId", 1L,
                                "dateTo", "2025-01-01"
                        ),
                        1
                ),
                Arguments.of(
                        Map.of(
                                "memberId", 1L,
                                "themeId", 1L,
                                "dateFrom", "2025-01-01"
                        ),
                        1
                ),
                Arguments.of(
                        Map.of(),
                        1
                ),
                Arguments.of(
                        Map.of(
                                "dateFrom", "2025-01-05",
                                "dateTo", "2025-01-30"
                        ),
                        0
                ),
                Arguments.of(
                        Map.of(
                                "memberId", 2L,
                                "themeId", 1L,
                                "dateFrom", "2025-01-01",
                                "dateTo", "2025-01-01"
                        ),
                        0
                )
        );
    }
}
