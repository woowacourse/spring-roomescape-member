package roomescape.endpoint;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.notNullValue;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import roomescape.dto.MemberReservationRequest;
import roomescape.dto.ReservationResponse;
import roomescape.dto.ReservationTimeResponse;
import roomescape.dto.ThemeResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(properties = {"spring.config.location = classpath:application-test.yml"})
public class ReservationEndPointTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @AfterEach
    void cleanUp() {
        jdbcTemplate.update("DELETE FROM reservation");
    }

    @DisplayName("예약 목록을 조회하면 상태 코드 200과 예약 목록을 응답으로 반환한다.")
    @Test
    void getReservations() {
        List<ReservationResponse> responses = RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath()
                .getList(".", ReservationResponse.class);

        List<ReservationResponse> expected = List.of(new ReservationResponse(
                1L,
                "칸쵸와 알파고",
                LocalDate.now().minusDays(1),
                new ReservationTimeResponse(2L, LocalTime.parse("11:00:00")),
                new ThemeResponse(2L, "이름2", "설명2", "썸네일2")
        ));

        assertThat(responses)
                .isEqualTo(expected);
    }

    @DisplayName("예약을 추가하면 상태 코드 201와 추가된 객체를 반환한다.")
    @Test
    void addReservation() {
        LocalDate date = LocalDate.now().plusDays(1);
        MemberReservationRequest request = new MemberReservationRequest(
                "알파카",
                date,
                2L,
                2L
        );
        ReservationResponse expected = new ReservationResponse(
                2L,
                "알파카",
                date,
                new ReservationTimeResponse(2L, LocalTime.parse("11:00:00")),
                new ThemeResponse(2L, "이름2", "설명2", "썸네일2")
        );

        ReservationResponse response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .as(ReservationResponse.class);

        assertThat(response)
                .isEqualTo(expected);
    }

    @DisplayName("예약을 삭제하면 상태 코드 204를 반환한다.")
    @Test
    void deleteReservation() {
        RestAssured.given().log().all()
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .body(notNullValue());
    }

    @DisplayName("과거의 시간으로 예약을 추가하면 상태 코드 400을 반환한다.")
    @Test
    void validateReservationTimeIsFutureFail() {
        MemberReservationRequest past = new MemberReservationRequest(
                "알파카",
                LocalDate.now().minusDays(1),
                2L,
                2L
        );

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(past)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("중복된 시간으로 예약을 추가하면 상태 코드 400을 반환한다.")
    @Test
    void validateReservationIsDuplicatedFail() {
        MemberReservationRequest duplicated = new MemberReservationRequest(
                "알파카",
                LocalDate.now().plusDays(1),
                2L,
                2L
        );

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(duplicated)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(duplicated)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }
}
