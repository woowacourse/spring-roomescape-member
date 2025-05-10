package roomescape.admin.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import roomescape.admin.domain.dto.AdminReservationRequestDto;
import roomescape.reservationTime.ReservationTimeTestDataConfig;
import roomescape.theme.ThemeTestDataConfig;
import roomescape.user.AdminTestDataConfig;
import roomescape.user.MemberTestDataConfig;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        classes = {
                ThemeTestDataConfig.class,
                ReservationTimeTestDataConfig.class,
                MemberTestDataConfig.class,
                AdminTestDataConfig.class})
class AdminControllerTest {

    @Autowired
    private ThemeTestDataConfig themeTestDataConfig;
    @Autowired
    private ReservationTimeTestDataConfig reservationTimeTestDataConfig;
    @Autowired
    private MemberTestDataConfig memberTestDataConfig;
    @Autowired
    private AdminTestDataConfig adminTestDataConfig;

    @Nested
    @DisplayName("POST /admin/reservations 요청")
    class createReservation {

        private static LocalDate date;

        private final Long themeId = themeTestDataConfig.getSavedId();
        private final Long reservationTimeId = reservationTimeTestDataConfig.getSavedId();
        private final Long memberId = memberTestDataConfig.getSavedId();
        private final Long adminId = adminTestDataConfig.getSavedId();

        @BeforeAll
        public static void setUp() {
            date = LocalDate.now().plusDays(1);
        }

        @DisplayName("memberId의 role이 ROLE_MEMBER 일 때 201 CREATED 와 함께 member의 예약이 추가된다.")
        @Test
        void createReservation_success_byMemberId() {
            // given
            AdminReservationRequestDto dto = new AdminReservationRequestDto(date,
                    themeId,
                    reservationTimeId,
                    memberId);

            // when
            // then
            RestAssured.given().log().all()
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
                    adminId);

            // when
            // then
            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .body(dto)
                    .when().post("/admin/reservations")
                    .then().log().all()
                    .statusCode(HttpStatus.UNAUTHORIZED.value());
        }
    }
}
