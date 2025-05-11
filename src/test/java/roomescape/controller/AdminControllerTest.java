package roomescape.controller;

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
import roomescape.auth.CookieProvider;
import roomescape.auth.JwtTokenProvider;
import roomescape.dao.FakeMemberDaoImpl;
import roomescape.dao.FakeReservationDaoImpl;
import roomescape.dao.FakeReservationTimeDaoImpl;
import roomescape.dao.FakeThemeDaoImpl;
import roomescape.dao.TestDaoConfiguration;
import roomescape.domain.Member;
import roomescape.domain.MemberRole;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationDate;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.AdminReservationRequest;
import roomescape.dto.MemberResponse;
import roomescape.dto.ReservationResponseDto;
import roomescape.dto.ReservationTimeResponseDto;
import roomescape.dto.ThemeResponseDto;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(properties = "spring.sql.init.mode=never")
@Import(TestDaoConfiguration.class)
class AdminControllerTest {

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

    @DisplayName("관리자는 사용자의 식별자로 예약을 할 수 있다.")
    @Test
    void createAdminReservation() {
        //given
        Member member = Member.from(1L, "user", "userEmail", "1234", MemberRole.USER);
        memberDao.save(member);

        Member memberAdmin = Member.from(2L, "user", "userEmail", "1234", MemberRole.ADMIN);
        memberDao.save(memberAdmin);

        themeDao.saveTheme(new Theme(1L, "name", "des", "thumb"));
        reservationTimeDao.saveReservationTime(new ReservationTime(1L, LocalTime.of(10, 0)));

        String token = tokenProvider.createToken(memberAdmin);
        Cookie cookie = cookieProvider.createCookie("token", token);

        AdminReservationRequest request = new AdminReservationRequest(NOW_DATE, 1L, 1L, 1L);

        //when
        ReservationResponseDto actual = RestAssured
                .given()
                .log().all()
                .contentType(ContentType.JSON)
                .cookie(cookie.getName(), cookie.getValue())
                .body(request)
                .when()
                .post("/admin/reservations")
                .then()
                .log().all()
                .statusCode(201)
                .extract()
                .as(ReservationResponseDto.class);

        //then
        assertThat(actual).isNotNull();
    }

    @DisplayName("관리자는 예약자별, 테마별, 날짜별 검색 조건을 사용해 예약 검색이 가능하다.")
    @Test
    void getReservationsByCondition() {
        //given
        Member member = Member.from(1L, "user", "userEmail", "1234", MemberRole.USER);
        memberDao.save(member);

        Member memberAdmin = Member.from(2L, "user", "userEmail1", "1234", MemberRole.ADMIN);
        memberDao.save(memberAdmin);

        Theme theme = new Theme(1L, "name", "des", "thumb");
        themeDao.saveTheme(theme);

        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(10, 0));
        reservationTimeDao.saveReservationTime(reservationTime);

        reservationDao.saveReservation(
                new Reservation(
                        1L,
                        member,
                        new ReservationDate(LocalDate.of(2025, 5, 1)),
                        reservationTime,
                        theme
                ));
        reservationDao.saveReservation(
                new Reservation(
                        2L,
                        member,
                        new ReservationDate(LocalDate.of(2025, 5, 3)),
                        reservationTime,
                        theme
                ));
        reservationDao.saveReservation(
                new Reservation(
                        3L,
                        member,
                        new ReservationDate(LocalDate.of(2025, 5, 5)),
                        reservationTime,
                        theme
                ));

        String token = tokenProvider.createToken(memberAdmin);
        Cookie cookie = cookieProvider.createCookie("token", token);

        //when
        List<ReservationResponseDto> actual = RestAssured
                .given()
                .log().all()
                .contentType(ContentType.JSON)
                .cookie(cookie.getName(), cookie.getValue())
                .queryParam("themeId", 1L)
                .queryParam("memberId", 1L)
                .queryParam("dateFrom", "2025-05-01")
                .queryParam("dateTo", "2025-05-05")
                .when()
                .get("/admin/reservations/search/")
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
                        MemberResponse.from(member),
                        "2025-05-01",
                        ReservationTimeResponseDto.from(reservationTime),
                        ThemeResponseDto.from(theme)
                ),
                new ReservationResponseDto(
                        2L,
                        MemberResponse.from(member),
                        "2025-05-03",
                        ReservationTimeResponseDto.from(reservationTime),
                        ThemeResponseDto.from(theme)
                ),
                new ReservationResponseDto(
                        3L,
                        MemberResponse.from(member),
                        "2025-05-05",
                        ReservationTimeResponseDto.from(reservationTime),
                        ThemeResponseDto.from(theme)
                )
        );

        assertThat(actual).hasSize(3).isEqualTo(compareList);

    }
}
