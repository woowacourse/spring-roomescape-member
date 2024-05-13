package roomescape.controller.api.reservation;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import roomescape.LoginUtils;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.repository.member.MemberRepository;
import roomescape.repository.reservation.ReservationRepository;
import roomescape.repository.reservationtime.ReservationTimeRepository;
import roomescape.repository.theme.ThemeRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static roomescape.InitialDataFixture.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class AdminReservationControllerTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ThemeRepository themeRepository;
    @Autowired
    private ReservationTimeRepository reservationTimeRepository;
    @Autowired
    private ReservationRepository reservationRepository;

    @Test
    @DisplayName("관리자 사용자가 예약을 추가한다.")
    void addReservationTest () {
        Member admin = memberRepository.save(ADMIN_1);
        String token = LoginUtils.loginAndGetToken(ADMIN_1);
        Theme theme = themeRepository.save(THEME_1);
        ReservationTime reservationTime = reservationTimeRepository.save(RESERVATION_TIME_1);

        Map<String, Object> reservationInfo = new HashMap<>();
        reservationInfo.put("memberId", admin.getId());
        reservationInfo.put("date", LocalDate.now().plusDays(1).format(DateTimeFormatter.ISO_DATE));
        reservationInfo.put("themeId", theme.getId());
        reservationInfo.put("timeId", reservationTime.getId());

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .body(reservationInfo)
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());

        LoginUtils.logout(ADMIN_1);
    }

    @DisplayName("조건에 맞는 예약을 가져온다.")
    @Test
    void getReservationsTest() {
        Member admin = memberRepository.save(ADMIN_1);
        String token = LoginUtils.loginAndGetToken(ADMIN_1);
        Theme theme = themeRepository.save(THEME_1);
        ReservationTime reservationTime = reservationTimeRepository.save(RESERVATION_TIME_1);

        reservationRepository.save(new Reservation(admin, LocalDate.now().plusDays(1), reservationTime, theme));
        reservationRepository.save(new Reservation(admin, LocalDate.now().plusDays(10), reservationTime, theme));

        Map<String, String> conditionParams = new HashMap<>();
        String format = LocalDate.now().plusDays(2).format(DateTimeFormatter.ISO_DATE);
        conditionParams.put("dateFrom", format);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .params(conditionParams)
                .when().get("/admin/reservations")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(1));

        LoginUtils.logout(ADMIN_1);
    }
}