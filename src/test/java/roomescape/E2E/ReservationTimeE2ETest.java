package roomescape.E2E;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import roomescape.controller.dto.request.CreateReservationTimeRequest;
import roomescape.controller.dto.response.ReservationTimeResponse;
import roomescape.domain.MemberRoleType;
import roomescape.jwt.JwtProvider;
import roomescape.jwt.JwtRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@Sql(scripts = "/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/reservation-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationTimeE2ETest {

    @Autowired
    JwtProvider jwtProvider;

    String token;

    @BeforeEach
    void init() {
        token = jwtProvider.generateToken(new JwtRequest(2, "admin", MemberRoleType.ADMIN, new Date()));
    }

    @Test
    @DisplayName("예약 가능 시간을 추가한다")
    void addReservationTime() {
        //given
        CreateReservationTimeRequest request = new CreateReservationTimeRequest(LocalTime.of(12, 34));

        //when //then
        RestAssured.given().log().all()
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/times")
                .then().log().all()
                .statusCode(201)
                .body("id", is(3));
    }

    @Test
    @DisplayName("같은 시간을 추가하려는 경우 409를 응답한다")
    void addDuplicatedReservationTime() {
        //given
        CreateReservationTimeRequest request = new CreateReservationTimeRequest(LocalTime.of(10, 0));

        //when //then
        RestAssured.given().log().all()
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/times")
                .then().log().all()
                .statusCode(409);
    }

    @Test
    @DisplayName("예약 시간을 삭제한다")
    void removeReservationTime() {
        //given //when //then
        RestAssured.given().log().all()
                .cookie("token", token)
                .when().delete("/times/2")
                .then().log().all()
                .statusCode(204);
    }

    @Test
    @DisplayName("예약이 존재하는 시간을 삭제하려는 경우 422를 응답한다")
    void removeUsedReservationTime() {
        //given //when //then
        RestAssured.given().log().all()
                .cookie("token", token)
                .when().delete("/times/1")
                .then().log().all()
                .statusCode(422);
    }

    @Test
    @DisplayName("모든 예약 가능 시간을 조회한다")
    void findAllReservationTime() {
        //given //when
        List<ReservationTimeResponse> actual = RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .extract().jsonPath().getList("", ReservationTimeResponse.class);

        //then
        assertThat(actual).hasSize(2);
    }
}
