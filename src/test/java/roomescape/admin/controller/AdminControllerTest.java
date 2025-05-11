package roomescape.admin.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import roomescape.admin.domain.dto.AdminReservationRequestDto;
import roomescape.auth.domain.dto.TokenResponseDto;
import roomescape.auth.fixture.AuthFixture;
import roomescape.auth.service.AuthService;
import roomescape.reservationTime.ReservationTimeTestDataConfig;
import roomescape.theme.ThemeTestDataConfig;
import roomescape.user.AdminTestDataConfig;
import roomescape.user.MemberTestDataConfig;
import roomescape.user.domain.User;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT,
        classes = {
                ThemeTestDataConfig.class,
                ReservationTimeTestDataConfig.class,
                MemberTestDataConfig.class,
                AdminTestDataConfig.class})
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class AdminControllerTest {

    @Autowired
    private ThemeTestDataConfig themeTestDataConfig;
    @Autowired
    private ReservationTimeTestDataConfig reservationTimeTestDataConfig;

    @LocalServerPort
    int port;

    @BeforeEach
    void restAssuredSetUp() {
        RestAssured.port = port;
    }

    @Nested
    @DisplayName("POST /admin/reservations 요청")
    class createReservation {

        private static LocalDate date;

        private final Long themeId = themeTestDataConfig.getSavedId();
        private final Long reservationTimeId = reservationTimeTestDataConfig.getSavedId();

        private static User memberStatic;
        private static User adminStatic;
        private static TokenResponseDto memberTokenResponseDto;
        private static TokenResponseDto adminTokenResponseDto;

        @BeforeAll
        public static void setUp(@Autowired AuthService authService,
                                 @Autowired MemberTestDataConfig memberTestDataConfig,
                                 @Autowired AdminTestDataConfig adminTestDataConfig
        ) {
            date = LocalDate.now().plusDays(1);

            memberStatic = memberTestDataConfig.getSavedMember();
            adminStatic = adminTestDataConfig.getSavedAdmin();

            memberTokenResponseDto = authService.login(
                    AuthFixture.createTokenRequestDto(memberStatic.getEmail(), memberStatic.getPassword()));
            adminTokenResponseDto = authService.login(
                    AuthFixture.createTokenRequestDto(adminStatic.getEmail(), adminStatic.getPassword()));
        }

        @DisplayName("memberId의 role이 ROLE_MEMBER 일 때 201 CREATED 와 함께 member의 예약이 추가된다.")
        @Test
        void createReservation_success_byMemberId() {
            // given
            AdminReservationRequestDto dto = new AdminReservationRequestDto(date,
                    themeId,
                    reservationTimeId,
                    memberStatic.getId());

            String token = adminTokenResponseDto.accessToken();

            // when
            // then
            RestAssured.given().log().all()
                    .cookies("token", token)
                    .contentType(ContentType.JSON)
                    .body(dto)
                    .when().post("/admin/reservations")
                    .then().log().all()
                    .statusCode(HttpStatus.CREATED.value());
        }

        @DisplayName("memberId에 role이 ROLE_ADMIN 일 때 401 A를 반환한다")
        @Test
        void createReservation_throwException_byAdminId() {
            // given
            AdminReservationRequestDto dto = new AdminReservationRequestDto(date,
                    themeId,
                    reservationTimeId,
                    adminStatic.getId());

            String token = adminTokenResponseDto.accessToken();

            // when
            // then
            RestAssured.given().log().all()
                    .cookies("token", token)
                    .contentType(ContentType.JSON)
                    .body(dto)
                    .when().post("/admin/reservations")
                    .then().log().all()
                    .statusCode(HttpStatus.UNAUTHORIZED.value());
        }
    }
}
