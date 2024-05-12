package roomescape.reservation;

import static org.hamcrest.Matchers.is;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import roomescape.auth.dto.LoginRequestDto;
import roomescape.auth.service.AuthService;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;
import roomescape.member.dto.MemberRequestDto;
import roomescape.member.service.MemberService;
import roomescape.reservation.dao.ReservationDao;
import roomescape.reservation.dto.ReservationRequestDto;
import roomescape.theme.dao.ThemeDao;
import roomescape.theme.domain.Theme;
import roomescape.time.dao.ReservationTimeDao;
import roomescape.time.domain.ReservationTime;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql(scripts = {"/schema.sql"})
public class ReservationAcceptanceTest {

    @LocalServerPort
    private int port;
    @Autowired
    private ReservationTimeDao reservationTimeDao;
    @Autowired
    private ThemeDao themeDao;
    @Autowired
    private MemberService memberService;
    @Autowired
    private AuthService authService;
    @Autowired
    private ReservationDao reservationDao;
    private String token;
    private final ReservationRequestDto reservationRequestDto = new ReservationRequestDto("2024-04-01", 1, 1);
    private final Theme theme = new Theme(1L, "정글모험", "정글모험 설명", "정글모험 이미지");
    private final ReservationTime reservationTime = new ReservationTime(1L, "10:00");
    private final Member member = new Member(1L, "hotea", "hotea@hotea.com", "1234", Role.USER);

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        reservationTimeDao.save(reservationTime);
        themeDao.save(theme);
        memberService.save(new MemberRequestDto("hotea@hotea.com", "1234", "hotea", Role.ADMIN));
        token = authService.login(new LoginRequestDto("1234", "hotea@hotea.com"));
    }

    @DisplayName("예약 요청이 들어올 시 저장할 수 있다.")
    @Test
    void save() {
        ReservationRequestDto reservationRequestDto = new ReservationRequestDto(LocalDate.now().plusDays(1)
                .toString(), 1, 1);
        RestAssured.given()
                .contentType(ContentType.JSON)
                .header("Cookie", "token=" + token)
                .body(reservationRequestDto)
                .when().post("/reservations")
                .then().statusCode(201);

        RestAssured.given()
                .when().get("/reservations")
                .then().statusCode(200)
                .body("size()", is(1));
    }

    @DisplayName("모든 예약을 조회할 수 있다.")
    @Test
    void findAll() {
        reservationDao.save(reservationRequestDto.toReservation(member, reservationTime, theme));

        RestAssured.given()
                .when().get("/reservations")
                .then().statusCode(200)
                .body("size()", is(1));
    }

    @DisplayName("중복 예약의 경우 예약에 실패한다.")
    @Test
    void duplicateSave() {
        reservationDao.save(reservationRequestDto.toReservation(member, reservationTime, theme));

        RestAssured.given()
                .contentType(ContentType.JSON)
                .header("Cookie", "token=" + token)
                .body(reservationRequestDto)
                .when().post("/reservations")
                .then().statusCode(400);
    }

    @DisplayName("예약을 삭제할 수 있다.")
    @Test
    void delete() {
        reservationDao.save(reservationRequestDto.toReservation(member, reservationTime, theme));

        RestAssured.given()
                .when().delete("/reservations/1")
                .then().statusCode(200);
    }

    @DisplayName("예약을 검색할 수 있다.")
    @Test
    void search() {
        for (int i = 1; i <= 4; i++) {
            ReservationRequestDto requestDto = new ReservationRequestDto(LocalDate.now().minusDays(i).toString(), 1, 1);
            reservationDao.save(requestDto.toReservation(member, reservationTime, theme));
        }
        RestAssured.given()
                .param("themeId", theme.getId())
                .param("memberId", member.getId())
                .param("dateFrom", LocalDate.now().minusDays(7).toString())
                .param("dateTo", LocalDate.now().toString())
                .when().get("/reservations/search")
                .then().statusCode(200)
                .body("size()", is(4));
    }
}
