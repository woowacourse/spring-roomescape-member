package roomescape.controller;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import java.time.LocalDate;
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
import roomescape.dao.FakeReservationDaoImpl;
import roomescape.dao.FakeReservationTimeDaoImpl;
import roomescape.dao.FakeThemeDaoImpl;
import roomescape.dao.TestDaoConfiguration;
import roomescape.domain.Person;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationDate;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.AvailableReservationTimeRequestDto;
import roomescape.dto.AvailableReservationTimeResponseDto;
import roomescape.dto.ReservationRequestDto;
import roomescape.dto.ReservationResponseDto;
import roomescape.dto.ReservationTimeResponseDto;
import roomescape.dto.ThemeResponseDto;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(properties = "spring.sql.init.mode=never")
@Import(TestDaoConfiguration.class)
class ReservationControllerTest {

    @Autowired
    private FakeReservationDaoImpl reservationDao;

    @Autowired
    private FakeReservationTimeDaoImpl reservationTimeDao;

    @Autowired
    private FakeThemeDaoImpl themeDao;

    @LocalServerPort
    int port;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
    }

    @DisplayName("예약을 등록하면 응답 예약이 반환된다.")
    @Test
    void createReservation() {
        //given
        reservationTimeDao.saveReservationTime(new ReservationTime(LocalTime.of(10, 0)));
        themeDao.saveTheme(new Theme("테마", "설명", "썸네일"));
        ReservationRequestDto request = new ReservationRequestDto("도기", "2025-05-07", 1L, 1L);

        //when
        ReservationResponseDto actual = RestAssured
                .given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/reservations")
                .then()
                .log().all()
                .statusCode(201)
                .extract()
                .as(ReservationResponseDto.class);

        //then
        assertThat(actual)
                .extracting("id", "name", "date", "time", "theme")
                .containsExactly(
                        1L,
                        "도기",
                        "2025-05-07",
                        new ReservationTimeResponseDto(
                                1L,
                                "10:00"),
                        new ThemeResponseDto(
                                1L,
                                "테마",
                                "설명",
                                "썸네일")
                );
    }

    @DisplayName("등록한 모든 예약을 조회하면 응답 예약 리스트가 반환된다.")
    @Test
    void readAllReservations() {
        //given
        ReservationTime reservationTime = new ReservationTime(LocalTime.of(10, 0));
        reservationTimeDao.saveReservationTime(reservationTime);

        Theme theme = new Theme("테마", "설명", "썸네일");
        themeDao.saveTheme(theme);

        reservationDao.saveReservation(new Reservation(
                new Person("도기"),
                new ReservationDate(LocalDate.of(2025, 5, 8)),
                reservationTime,
                theme));

        reservationDao.saveReservation(new Reservation(
                new Person("도기"),
                new ReservationDate(LocalDate.of(2025, 5, 9)),
                reservationTime,
                theme));

        //when
        List<ReservationResponseDto> actual = RestAssured
                .given()
                .log().all()
                .contentType(ContentType.JSON)
                .when()
                .get("/reservations")
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .as(new TypeRef<>() {
                });

        //then
        List<ReservationResponseDto> compareList = List.of(
                new ReservationResponseDto(
                        1L,
                        "도기",
                        "2025-05-08",
                        new ReservationTimeResponseDto(1L, "10:00"),
                        new ThemeResponseDto(1L, "테마", "설명", "썸네일")
                ),
                new ReservationResponseDto(
                        2L,
                        "도기",
                        "2025-05-09",
                        new ReservationTimeResponseDto(1L, "10:00"),
                        new ThemeResponseDto(1L, "테마", "설명", "썸네일")
                )
        );
        assertThat(actual)
                .hasSize(2)
                .isEqualTo(compareList);
    }

    @DisplayName("등록된 예약을 삭제할 수 있다.")
    @Test
    void deleteReservation() {
        //given
        ReservationTime reservationTime = new ReservationTime(LocalTime.of(10, 0));
        reservationTimeDao.saveReservationTime(reservationTime);

        Theme theme = new Theme("테마", "설명", "썸네일");
        themeDao.saveTheme(theme);

        reservationDao.saveReservation(new Reservation(
                new Person("도기"),
                new ReservationDate(LocalDate.of(2025, 5, 8)),
                reservationTime,
                theme));

        //when
        RestAssured
                .given()
                .log().all()
                .contentType(ContentType.JSON)
                .when()
                .delete("/reservations/1")
                .then()
                .log().all()
                .statusCode(204);

        //then
        List<Reservation> actual = reservationDao.findAllReservation();
        assertThat(actual).hasSize(0);
    }

    @DisplayName("특정 날짜와 테마에 대해 예약 가능한 시간을 조회하면 모든 시간에 대해서 예약 가능 여부를 포함해 응답으로 반환한다.")
    @Test
    void readAvailableReservationTimes() {
        //given
        ReservationTime reservationTime = new ReservationTime(LocalTime.of(10, 0));
        reservationTimeDao.saveReservationTime(reservationTime);

        ReservationTime reservationTime2 = new ReservationTime(LocalTime.of(10, 5));
        reservationTimeDao.saveReservationTime(reservationTime2);

        Theme theme = new Theme("테마", "설명", "썸네일");
        themeDao.saveTheme(theme);

        reservationDao.saveReservation(new Reservation(
                new Person("도기"),
                new ReservationDate(LocalDate.of(2025, 5, 8)),
                reservationTime,
                theme));

        AvailableReservationTimeRequestDto request = new AvailableReservationTimeRequestDto(
                LocalDate.of(2025, 5, 8), 1L);

        //when
        List<AvailableReservationTimeResponseDto> actual = RestAssured
                .given()
                .log().all()
                .contentType(ContentType.JSON)
                .when()
                .body(request)
                .post("/reservations/available-times")
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .as(new TypeRef<>() {
                });

        //then
        List<AvailableReservationTimeResponseDto> compareList = List.of(
                new AvailableReservationTimeResponseDto(
                        "10:00",
                        1L,
                        true
                ),
                new AvailableReservationTimeResponseDto(
                        "10:05",
                        2L,
                        false
                )
        );
        assertThat(actual)
                .hasSize(2)
                .isEqualTo(compareList);
    }


}
