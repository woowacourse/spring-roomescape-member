package roomescape.controller.reservation;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import jakarta.servlet.http.Cookie;
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
import roomescape.common.auth.CookieProvider;
import roomescape.common.auth.JwtTokenProvider;
import roomescape.dao.TestDaoConfiguration;
import roomescape.dao.member.FakeMemberDaoImpl;
import roomescape.dao.reservation.FakeReservationDaoImpl;
import roomescape.dao.reservationtime.FakeReservationTimeDaoImpl;
import roomescape.dao.theme.FakeThemeDaoImpl;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberRole;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationDate;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.domain.theme.Theme;
import roomescape.dto.member.response.MemberResponse;
import roomescape.dto.reservation.request.AvailableReservationTimeRequestDto;
import roomescape.dto.reservation.request.ReservationRequestDto;
import roomescape.dto.reservation.response.AvailableReservationTimeResponseDto;
import roomescape.dto.reservation.response.ReservationResponseDto;
import roomescape.dto.reservationtime.response.ReservationTimeResponseDto;
import roomescape.dto.theme.response.ThemeResponseDto;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(properties = "spring.sql.init.mode=never")
@Import(TestDaoConfiguration.class)
class ReservationControllerTest {

    private static final LocalDate NOW_DATE = LocalDate.now().plusDays(1);

    @Autowired
    private FakeReservationDaoImpl reservationDao;

    @Autowired
    private FakeReservationTimeDaoImpl reservationTimeDao;

    @Autowired
    private FakeThemeDaoImpl themeDao;

    @Autowired
    private FakeMemberDaoImpl memberDao;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private CookieProvider cookieProvider;

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
        Member member = Member.from(1L, "testName", "testEmail", "1234", MemberRole.USER);
        memberDao.save(member);
        ReservationTime reservationTime = new ReservationTime(LocalTime.of(10, 0));
        reservationTimeDao.saveReservationTime(reservationTime);
        themeDao.saveTheme(new Theme("테마", "설명", "썸네일"));
        ReservationRequestDto request = new ReservationRequestDto(NOW_DATE.toString(), 1L, 1L);

        String token = tokenProvider.createToken(member);
        Cookie cookie = cookieProvider.createCookie("token", token);

        //when
        ReservationResponseDto actual = RestAssured
                .given()
                .log().all()
                .contentType(ContentType.JSON)
                .cookie(cookie.getName(), cookie.getValue())
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
                .extracting("id", "member", "date", "time", "theme")
                .containsExactly(
                        1L,
                        MemberResponse.from(member),
                        NOW_DATE.toString(),
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
        Member member1 = Member.from(1L, "testName1", "testEmail1", "1234", MemberRole.USER);
        Member member2 = Member.from(2L, "testName2", "testEmail2", "1234", MemberRole.USER);

        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(10, 0));
        Theme theme = new Theme(1L, "테마", "설명", "썸네일");

        reservationDao.saveReservation(new Reservation(
                member1,
                new ReservationDate(NOW_DATE),
                reservationTime,
                theme));

        reservationDao.saveReservation(new Reservation(
                member2,
                new ReservationDate(NOW_DATE.plusDays(1)),
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
                        MemberResponse.from(member1),
                        NOW_DATE.toString(),
                        new ReservationTimeResponseDto(1L, "10:00"),
                        new ThemeResponseDto(1L, "테마", "설명", "썸네일")
                ),
                new ReservationResponseDto(
                        2L,
                        MemberResponse.from(member2),
                        NOW_DATE.plusDays(1).toString(),
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
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(10, 0));
        Theme theme = new Theme(1L, "테마", "설명", "썸네일");

        reservationDao.saveReservation(new Reservation(
                Member.from(1L, "testName", "testEmail", "1234", MemberRole.USER),
                new ReservationDate(NOW_DATE),
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
                Member.from(1L, "testName", "testEmail", "1234", MemberRole.USER),
                new ReservationDate(NOW_DATE),
                reservationTime,
                theme));

        AvailableReservationTimeRequestDto request = new AvailableReservationTimeRequestDto(NOW_DATE, 1L);

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
