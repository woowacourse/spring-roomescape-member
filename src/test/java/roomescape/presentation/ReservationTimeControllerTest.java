package roomescape.presentation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import roomescape.TestRepositoryConfig;
import roomescape.business.ReservationTime;
import roomescape.persistence.fakerepository.FakeReservationTimeRepository;
import roomescape.presentation.dto.ReservationTimeRequestDto;
import roomescape.presentation.dto.ReservationTimeResponseDto;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = "spring.sql.init.mode=never")
@ActiveProfiles("test")
@Import(TestRepositoryConfig.class)
class ReservationTimeControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private FakeReservationTimeRepository reservationTimeRepository;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @AfterEach
    void tearDown() {
        reservationTimeRepository.clear();
    }

    @DisplayName("예약 가능한 시간 목록을 조회하면 상태 코드와 함께 시간 목록이 반환된다.")
    @Test
    void getReservationTimes() {
        // given
        reservationTimeRepository.add(new ReservationTime(LocalTime.of(10, 0)));

        // when
        List<ReservationTimeResponseDto> response = RestAssured
                .given()
                .log().all()
                .contentType(ContentType.JSON)
                .when()
                .get("/times")
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList(".", ReservationTimeResponseDto.class);

        // then
        assertAll(
                () -> assertThat(response)
                        .hasSize(1),
                () -> assertThat(response.get(0).startAt())
                        .isEqualTo(LocalTime.of(10, 0))
        );
    }

    @DisplayName("예약 가능한 시간을 조회하면 상태 코드와 함께 예약 시간이 반환된다.")
    @Test
    void getReservationTimeById() {
        // given
        ReservationTime reservationTime = new ReservationTime(LocalTime.of(10, 0));
        Long id = reservationTimeRepository.add(reservationTime);

        // when
        ReservationTimeResponseDto response = RestAssured
                .given()
                .log().all()
                .contentType(ContentType.JSON)
                .when()
                .get("/times/" + id)
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .as(ReservationTimeResponseDto.class);

        // then
        assertAll(
                () -> assertThat(response.id())
                        .isNotNull()
                        .isEqualTo(id),
                () -> assertThat(response.startAt())
                        .isEqualTo(LocalTime.of(10, 0))
        );
    }

    @DisplayName("예약 가능한 시간을 생성하면 상태 코드와 함께 생성된 예약 시간이 반환된다.")
    @Test
    void createReservationTime() {
        // given
        ReservationTimeRequestDto requestDto = new ReservationTimeRequestDto(LocalTime.of(20, 0));

        // when
        ReservationTimeResponseDto response = RestAssured
                .given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(requestDto)
                .when()
                .post("/times")
                .then()
                .log().all()
                .statusCode(201)
                .extract()
                .as(ReservationTimeResponseDto.class);

        // then
        assertAll(
                () -> assertThat(response.id())
                        .isNotNull(),
                () -> assertThat(response.startAt())
                        .isEqualTo(LocalTime.of(20, 0))
        );
    }

    @DisplayName("예약 가능한 시간을 삭제하면 상태 코드 204가 반환된다.")
    @Test
    void deleteReservationTime() {
        // given
        ReservationTime reservationTime = new ReservationTime(LocalTime.of(14, 0));
        Long id = reservationTimeRepository.add(reservationTime);

        // when
        RestAssured
                .given()
                .log().all()
                .when()
                .delete("/times/" + id)
                .then()
                .log().all()
                .statusCode(204);

        // then
        assertThat(reservationTimeRepository.findById(id))
                .isEmpty();
    }
}
