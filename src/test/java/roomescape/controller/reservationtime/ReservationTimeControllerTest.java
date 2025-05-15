package roomescape.controller.reservationtime;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import roomescape.dao.reservationtime.FakeReservationTimeDaoImpl;
import roomescape.dao.TestDaoConfiguration;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.dto.reservationtime.request.ReservationTimeRequestDto;
import roomescape.dto.reservationtime.response.ReservationTimeResponseDto;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(properties = "spring.sql.init.mode=never")
@Import(TestDaoConfiguration.class)
class ReservationTimeControllerTest {

    @Autowired
    private FakeReservationTimeDaoImpl reservationTimeDao;

    @LocalServerPort
    int port;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
    }

    @DisplayName("예약 시간을 등록하면 응답 예약 시간이 반환된다.")
    @Test
    void saveReservationTime() {
        //given
        ReservationTimeRequestDto request = new ReservationTimeRequestDto("10:00");

        //when
        ReservationTimeResponseDto actual = RestAssured
                .given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/times")
                .then()
                .log().all()
                .statusCode(201)
                .extract()
                .as(ReservationTimeResponseDto.class);

        //then
        assertThat(actual)
                .extracting("id", "startAt")
                .containsExactly(1L, "10:00");
    }

    @DisplayName("등록한 모든 예약 시간을 조회하면 응답 예약 시간 리스트가 반환된다.")
    @Test
    void readReservationTimes() {
        //given
        reservationTimeDao.saveReservationTime(new ReservationTime(1L, LocalTime.of(10, 0)));
        reservationTimeDao.saveReservationTime(new ReservationTime(2L, LocalTime.of(10, 5)));

        //when
        List<ReservationTimeResponseDto> actual = RestAssured
                .given()
                .log().all()
                .contentType(ContentType.JSON)
                .when()
                .get("/times")
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .as(new TypeRef<>() {
                });

        //then
        List<ReservationTimeResponseDto> compareList = List.of(
                new ReservationTimeResponseDto(1L, "10:00"),
                new ReservationTimeResponseDto(2L, "10:05"));
        assertThat(actual)
                .hasSize(2)
                .isEqualTo(compareList);
    }

    @DisplayName("등록한 예약을 삭제할 수 있다.")
    @Test
    void deleteReservationTime() {
        //given
        reservationTimeDao.saveReservationTime(new ReservationTime(1L, LocalTime.of(10, 0)));

        //when
        RestAssured
                .given()
                .log().all()
                .contentType(ContentType.JSON)
                .when()
                .delete("/times/1")
                .then()
                .log().all()
                .statusCode(204);

        //then
        List<ReservationTime> actual = reservationTimeDao.findAllReservationTimes();
        assertThat(actual).hasSize(0);
    }

}
