package roomescape.admin.controller;

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
import roomescape.admin.dto.AdminReservationRequestDto;
import roomescape.auth.dto.LoginRequestDto;
import roomescape.auth.service.AuthService;
import roomescape.member.domain.Role;
import roomescape.member.dto.MemberRequestDto;
import roomescape.member.service.MemberService;
import roomescape.theme.dao.ThemeDao;
import roomescape.theme.domain.Theme;
import roomescape.time.dao.ReservationTimeDao;
import roomescape.time.domain.ReservationTime;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql(scripts = {"/schema.sql"})
class AdminAcceptanceTest {
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
    private String token;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        reservationTimeDao.save(new ReservationTime("10:00"));
        themeDao.save(new Theme("정글모험", "정글모험 설명", "정글모험 이미지"));
        memberService.save(new MemberRequestDto("hotea@hotea.com", "1234", "hotea", Role.ADMIN));
        token = authService.login(new LoginRequestDto("1234", "hotea@hotea.com"));
    }

    @DisplayName("admin 권한을 가진 사용자가 임의로 예약을 생성할 수 있다.")
    @Test
    void registerReservation() {
        AdminReservationRequestDto requestDto = new AdminReservationRequestDto(LocalDate.MAX.toString(), 1, 1, 1);
        RestAssured.given()
                .contentType(ContentType.JSON)
                .header("Cookie", "token=" + token)
                .body(requestDto)
                .when().post("/admin/reservations")
                .then().statusCode(201);
    }
}
